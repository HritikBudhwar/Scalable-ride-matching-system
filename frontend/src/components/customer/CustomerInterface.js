import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { 
  Car, 
  MapPin, 
  Clock, 
  CreditCard, 
  User, 
  Home, 
  History, 
  Settings,
  Phone,
  Mail,
  Star,
  ArrowLeft,
  Search,
  Navigation,
  DollarSign
} from 'lucide-react';
import toast from 'react-hot-toast';
import { customerAPI, bookingAPI, mapAPI } from '../../services/api';
import PaymentPage from './PaymentPage';

const CustomerInterface = ({ userInfo, onLogout }) => {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('home');
  const [bookingForm, setBookingForm] = useState({
    source: '',
    destination: '',
    serviceType: 'SEDAN',
    vehicleCategory: 'SEDAN'
  });
  const [searchResults, setSearchResults] = useState([]);
  const [activeBooking, setActiveBooking] = useState(null);
  const [activeTrip, setActiveTrip] = useState(null);
  const [customerBookings, setCustomerBookings] = useState([]);
  const [customerTrips, setCustomerTrips] = useState([]);
  const [rideOtp, setRideOtp] = useState('');
  const [routeDistanceKm, setRouteDistanceKm] = useState(null);
  const [mapUrl, setMapUrl] = useState('');
  const [loading, setLoading] = useState(false);
  const [showPayment, setShowPayment] = useState(false);
  const [completedTrip, setCompletedTrip] = useState(null);
  const customerId = userInfo?.userId;

  // Load customer data on component mount
  useEffect(() => {
    if (customerId) {
      loadCustomerBookings();
      loadCustomerTrips();
    }
  }, []);

  // Poll active trip status so customer sees driver acceptance
  useEffect(() => {
    if (!activeTrip?.id) return;

    const poll = async () => {
      try {
        const res = await customerAPI.getTripById(activeTrip.id);
        setActiveTrip(res.data);
        if (res.data.status === 'ASSIGNED') {
          const otpResponse = await customerAPI.getRideOtp(activeTrip.id, customerId);
          setRideOtp(otpResponse.data.otp || '');
        } else if (res.data.status === 'COMPLETED') {
          // Trip completed by driver — redirect customer to payment
          setCompletedTrip(res.data);
          setShowPayment(true);
          setActiveBooking(null);
          setActiveTrip(null);
          setRideOtp('');
          toast.success('Your ride is completed! Please proceed to payment.');
          return; // Stop polling
        } else {
          setRideOtp('');
        }
      } catch (e) {
        // ignore transient poll errors
      }
    };

    poll();
    const interval = setInterval(poll, 3000);
    return () => clearInterval(interval);
  }, [activeTrip?.id]);

  const loadCustomerBookings = async () => {
    try {
      setLoading(true);
      const response = await customerAPI.getCustomerBookings(customerId);
      setCustomerBookings(response.data);
    } catch (error) {
      console.error('Failed to load bookings:', error);
      toast.error('Failed to load bookings');
    } finally {
      setLoading(false);
    }
  };

  const loadCustomerTrips = async () => {
    try {
      setLoading(true);
      const response = await customerAPI.getTripHistory(customerId);
      setCustomerTrips(response.data);
    } catch (error) {
      console.error('Failed to load trips:', error);
      toast.error('Failed to load trips');
    } finally {
      setLoading(false);
    }
  };

  const serviceTypes = [
    { value: 'BIKE', label: 'Bike', price: '₹', time: '2-4 min' },
    { value: 'AUTO', label: 'Auto', price: '₹₹', time: '3-6 min' },
    { value: 'HATCHBACK', label: 'Hatchback', price: '₹₹₹', time: '4-8 min' },
    { value: 'SEDAN', label: 'Sedan', price: '₹₹₹₹', time: '5-10 min' },
    { value: 'SUV', label: 'SUV', price: '₹₹₹₹₹', time: '6-12 min' },
    { value: 'LOGISTICS', label: 'Logistics', price: '₹₹₹', time: 'Varies' }
  ];

  const recentTrips = [
    {
      id: 1,
      date: '2024-01-15',
      time: '14:30',
      source: 'Downtown Plaza',
      destination: 'Airport Terminal 2',
      driver: 'John Smith',
      fare: '₹425.50',
      status: 'completed',
      rating: 5
    },
    {
      id: 2,
      date: '2024-01-14',
      time: '09:15',
      source: 'Home',
      destination: 'Office Building',
      driver: 'Sarah Johnson',
      fare: '₹212.75',
      status: 'completed',
      rating: 4
    }
  ];

  const handleBooking = async () => {
    if (!bookingForm.source || !bookingForm.destination) {
      toast.error('Please enter both pickup and destination');
      return;
    }

    try {
      setLoading(true);

      // Step 1: Geocode addresses — show a clear error if location not found
      let sourceGeo, destinationGeo;
      try {
        sourceGeo = await mapAPI.geocodeAddress(bookingForm.source);
      } catch (e) {
        toast.error('Pickup location not found. Try a more specific address.');
        setLoading(false);
        return;
      }
      try {
        destinationGeo = await mapAPI.geocodeAddress(bookingForm.destination);
      } catch (e) {
        toast.error('Destination not found. Try a more specific address.');
        setLoading(false);
        return;
      }

      // Step 2: Get route distance — fall back gracefully if OSRM is unavailable
      let actualDistanceKm = 0;
      try {
        actualDistanceKm = await mapAPI.getRouteDistanceKm(sourceGeo, destinationGeo);
      } catch (e) {
        console.warn('Could not fetch route distance, proceeding without it:', e.message);
      }
      setRouteDistanceKm(actualDistanceKm || null);
      setMapUrl(`https://www.openstreetmap.org/directions?engine=fossgis_osrm_car&route=${sourceGeo.lat}%2C${sourceGeo.lon}%3B${destinationGeo.lat}%2C${destinationGeo.lon}`);

      // Step 3: Create booking on backend
      const bookingData = {
        source: `${sourceGeo.lat},${sourceGeo.lon}`,
        destination: `${destinationGeo.lat},${destinationGeo.lon}`,
        serviceType: bookingForm.serviceType,
        vehicleCategory: bookingForm.vehicleCategory
      };

      const response = await customerAPI.createBooking(bookingData, customerId);

      // Step 4: Confirm booking to create a SEARCHING trip visible to drivers
      const tripRes = await bookingAPI.confirmBooking(response.data.id);
      setActiveTrip(tripRes.data);

      setActiveBooking({
        ...response.data,
        sourceLabel: bookingForm.source,
        destinationLabel: bookingForm.destination,
        routeDistanceKm: actualDistanceKm || null,
        status: 'SEARCHING',
      });

      const distanceMsg = actualDistanceKm
        ? ` Distance: ${actualDistanceKm.toFixed(2)} km`
        : '';
      toast.success(`Booking created!${distanceMsg} Searching for drivers...`);
      setActiveTab('active');

      // Refresh bookings list
      loadCustomerBookings();
    } catch (error) {
      console.error('Booking error:', error);
      // Show backend message if available, otherwise a generic fallback
      const msg = error.response?.data?.message || error.message || 'Failed to create booking';
      toast.error(msg);
    } finally {
      setLoading(false);
    }
  };

  const handleCancelBooking = async () => {
    if (!activeBooking) return;

    try {
      setLoading(true);
      await customerAPI.cancelBooking(activeBooking.id, 'Customer cancelled');
      setActiveBooking(null);
      setActiveTrip(null);
      toast.success('Booking cancelled');
      setActiveTab('home');
      
      // Refresh bookings
      loadCustomerBookings();
    } catch (error) {
      console.error('Failed to cancel booking:', error);
      toast.error('Failed to cancel booking');
    } finally {
      setLoading(false);
    }
  };

  const handleContactDriver = () => {
    const phone = activeTrip?.driverPhone;
    if (!phone) {
      toast.error('Driver not assigned yet');
      return;
    }
    window.open(`tel:${phone}`, '_self');
  };

  const renderContent = () => {
    // Show payment page if trip was completed
    if (showPayment && completedTrip) {
      return (
        <PaymentPage
          trip={completedTrip}
          onPaymentComplete={() => {
            setShowPayment(false);
            setCompletedTrip(null);
            setActiveTab('home');
            loadCustomerTrips();
          }}
          onBack={() => {
            setShowPayment(false);
            setCompletedTrip(null);
            setActiveTab('home');
          }}
        />
      );
    }

    switch (activeTab) {
      case 'home':
        return (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="space-y-6"
          >
            {/* Booking Form */}
            <div className="card">
              <div className="p-6">
                <h3 className="text-xl font-semibold text-gray-900 mb-6">Book Your Ride</h3>
                
                <div className="space-y-4">
                  <div className="relative">
                    <MapPin className="absolute left-3 top-3 w-5 h-5 text-gray-400" />
                    <input
                      type="text"
                      placeholder="Enter pickup location"
                      className="input-field pl-10"
                      value={bookingForm.source}
                      onChange={(e) => setBookingForm({...bookingForm, source: e.target.value})}
                    />
                  </div>
                  
                  <div className="relative">
                    <Navigation className="absolute left-3 top-3 w-5 h-5 text-gray-400" />
                    <input
                      type="text"
                      placeholder="Where to?"
                      className="input-field pl-10"
                      value={bookingForm.destination}
                      onChange={(e) => setBookingForm({...bookingForm, destination: e.target.value})}
                    />
                  </div>

                  {/* Service Type Selection */}
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Service Type</label>
                    <div className="grid grid-cols-2 gap-3">
                      {serviceTypes.map((service) => (
                        <button
                          key={service.value}
                          onClick={() => setBookingForm({
                            ...bookingForm,
                            serviceType: service.value,
                            vehicleCategory: service.value
                          })}
                          className={`p-3 rounded-lg border-2 transition-all ${
                            bookingForm.serviceType === service.value
                              ? 'border-primary-500 bg-primary-50'
                              : 'border-gray-200 hover:border-gray-300'
                          }`}
                        >
                          <div className="text-left">
                            <div className="font-medium text-gray-900">{service.label}</div>
                            <div className="text-sm text-gray-500">{service.price} · {service.time}</div>
                          </div>
                        </button>
                      ))}
                    </div>
                  </div>

                  <button
                    onClick={handleBooking}
                    className="w-full btn-primary py-3 text-lg font-semibold"
                  >
                    Book Ride
                  </button>
                </div>
              </div>
            </div>

            {/* Quick Actions */}
            <div className="grid grid-cols-2 gap-4">
              <button className="card p-4 text-left hover:shadow-lg transition-shadow">
                <Clock className="w-6 h-6 text-primary-600 mb-2" />
                <div className="font-medium text-gray-900">Schedule Ride</div>
                <div className="text-sm text-gray-600">Book in advance</div>
              </button>
              <button className="card p-4 text-left hover:shadow-lg transition-shadow">
                <Star className="w-6 h-6 text-primary-600 mb-2" />
                <div className="font-medium text-gray-900">Saved Places</div>
                <div className="text-sm text-gray-600">Quick access</div>
              </button>
            </div>
          </motion.div>
        );

      case 'active':
        return (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="space-y-6"
          >
            {activeBooking ? (
              <div className="card">
                <div className="p-6">
                  <div className="flex items-center justify-between mb-6">
                    <h3 className="text-xl font-semibold text-gray-900">Active Booking</h3>
                    <span className="px-3 py-1 bg-yellow-100 text-yellow-800 rounded-full text-sm font-medium">
                      {activeBooking.status}
                    </span>
                  </div>

                  <div className="space-y-4">
                    {activeTrip && (
                      <div className="p-4 rounded-lg bg-blue-50 border border-blue-100">
                        <div className="flex items-center justify-between">
                          <div className="font-medium text-gray-900">Trip Status</div>
                          <div className="text-sm font-semibold text-blue-700">
                            {activeTrip.status}
                          </div>
                        </div>
                        <div className="mt-2 text-sm text-gray-700">
                          {activeTrip.driverName
                            ? `Driver assigned: ${activeTrip.driverName}. Arriving soon.`
                            : 'Searching for a driver...'}
                        </div>
                      </div>
                    )}

                    <div className="flex items-center space-x-3">
                      <MapPin className="w-5 h-5 text-gray-400" />
                      <div>
                        <div className="font-medium text-gray-900">From: {activeBooking.sourceLabel || activeBooking.source}</div>
                        <div className="text-sm text-gray-600">To: {activeBooking.destinationLabel || activeBooking.destination}</div>
                      </div>
                    </div>

                    {activeBooking.routeDistanceKm != null && (
                      <div className="p-3 bg-blue-50 rounded-lg text-sm text-blue-900">
                        Estimated route distance: <span className="font-semibold">{activeBooking.routeDistanceKm.toFixed(2)} km</span>
                      </div>
                    )}

                    {mapUrl && (
                      <a
                        href={mapUrl}
                        target="_blank"
                        rel="noreferrer"
                        className="block text-sm text-primary-600 hover:text-primary-700"
                      >
                        Open route on map
                      </a>
                    )}

                    {rideOtp && activeTrip?.status === 'ASSIGNED' && (
                      <div className="p-4 rounded-lg bg-amber-50 border border-amber-200">
                        <div className="text-sm text-amber-700">Share this OTP with driver at pickup</div>
                        <div className="text-2xl font-bold tracking-widest text-amber-900">{rideOtp}</div>
                      </div>
                    )}

                    <div className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                      <div>
                        <div className="text-sm text-gray-600">Estimated Fare</div>
                        <div className="text-2xl font-bold text-gray-900">₹{activeBooking.estimatedFare}</div>
                      </div>
                    </div>

                    <div className="flex space-x-3">
                      <button
                        onClick={handleCancelBooking}
                        className="flex-1 bg-red-600 hover:bg-red-700 text-white font-medium py-3 px-4 rounded-lg transition-colors"
                      >
                        Cancel Booking
                      </button>
                      <button
                        className="flex-1 btn-primary"
                        onClick={handleContactDriver}
                        disabled={!activeTrip?.driverPhone}
                        title={!activeTrip?.driverPhone ? 'Driver not assigned yet' : undefined}
                      >
                        Contact Driver
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            ) : (
              <div className="text-center py-12">
                <Car className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                <h3 className="text-lg font-medium text-gray-900 mb-2">No Active Bookings</h3>
                <p className="text-gray-600 mb-4">You don't have any active rides</p>
                <button
                  onClick={() => setActiveTab('home')}
                  className="btn-primary"
                >
                  Book a Ride
                </button>
              </div>
            )}
          </motion.div>
        );

      case 'history':
        return (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="space-y-4"
          >
            <h3 className="text-xl font-semibold text-gray-900 mb-4">Ride History</h3>
            {recentTrips.map((trip) => (
              <div key={trip.id} className="card">
                <div className="p-4">
                  <div className="flex items-center justify-between mb-3">
                    <div className="flex items-center space-x-3">
                      <div className="w-10 h-10 bg-primary-100 rounded-full flex items-center justify-center">
                        <Car className="w-5 h-5 text-primary-600" />
                      </div>
                      <div>
                        <div className="font-medium text-gray-900">{trip.source} to {trip.destination}</div>
                        <div className="text-sm text-gray-600">{trip.date} at {trip.time}</div>
                      </div>
                    </div>
                    <div className="text-right">
                      <div className="font-semibold text-gray-900">{trip.fare}</div>
                      <div className="flex items-center space-x-1">
                        {[...Array(5)].map((_, i) => (
                          <Star
                            key={i}
                            className={`w-4 h-4 ${
                              i < trip.rating ? 'text-yellow-400 fill-current' : 'text-gray-300'
                            }`}
                          />
                        ))}
                      </div>
                    </div>
                  </div>
                  <div className="flex items-center justify-between text-sm text-gray-600">
                    <span>Driver: {trip.driver}</span>
                    <span className="px-2 py-1 bg-green-100 text-green-800 rounded-full text-xs">
                      {trip.status}
                    </span>
                  </div>
                </div>
              </div>
            ))}
          </motion.div>
        );

      case 'profile':
        return (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="space-y-6"
          >
            <div className="card">
              <div className="p-6">
                <h3 className="text-xl font-semibold text-gray-900 mb-6">Profile Settings</h3>
                
                <div className="space-y-4">
                  <div className="flex items-center space-x-4">
                    <div className="w-20 h-20 bg-primary-100 rounded-full flex items-center justify-center">
                      <User className="w-10 h-10 text-primary-600" />
                    </div>
                    <div>
                      <div className="font-medium text-gray-900">
                        {userInfo?.firstName} {userInfo?.lastName}
                      </div>
                      <div className="text-sm text-gray-600">Premium Member</div>
                    </div>
                  </div>

                  <div className="space-y-3">
                    <div className="flex items-center space-x-3">
                      <Phone className="w-5 h-5 text-gray-400" />
                      <span className="text-gray-700">{userInfo?.phone || 'Phone: N/A'}</span>
                    </div>
                    <div className="flex items-center space-x-3">
                      <Mail className="w-5 h-5 text-gray-400" />
                      <span className="text-gray-700">{userInfo?.email || 'Email: N/A'}</span>
                    </div>
                  </div>

                  <button className="w-full btn-secondary">Edit Profile</button>
                </div>
              </div>
            </div>
          </motion.div>
        );

      default:
        return null;
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white border-b border-gray-200 sticky top-0 z-50">
        <div className="px-4 py-3">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-3">
              <button
                onClick={onLogout}
                className="p-2 hover:bg-gray-100 rounded-lg transition-colors"
              >
                <ArrowLeft className="w-5 h-5 text-gray-600" />
              </button>
              <h1 className="text-xl font-bold text-gray-900">Customer Portal</h1>
            </div>
            <div className="flex items-center space-x-3">
              <button className="p-2 hover:bg-gray-100 rounded-lg transition-colors">
                <Search className="w-5 h-5 text-gray-600" />
              </button>
              <button className="p-2 hover:bg-gray-100 rounded-lg transition-colors">
                <Settings className="w-5 h-5 text-gray-600" />
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="pb-20">
        <div className="p-4">
          {renderContent()}
        </div>
      </main>

      {/* Bottom Navigation */}
      <nav className="fixed bottom-0 left-0 right-0 bg-white border-t border-gray-200">
        <div className="grid grid-cols-4 py-2">
          <button
            onClick={() => setActiveTab('home')}
            className={`flex flex-col items-center py-2 px-3 transition-colors ${
              activeTab === 'home' ? 'text-primary-600' : 'text-gray-400'
            }`}
          >
            <Home className="w-6 h-6 mb-1" />
            <span className="text-xs">Home</span>
          </button>
          <button
            onClick={() => setActiveTab('active')}
            className={`flex flex-col items-center py-2 px-3 transition-colors ${
              activeTab === 'active' ? 'text-primary-600' : 'text-gray-400'
            }`}
          >
            <Car className="w-6 h-6 mb-1" />
            <span className="text-xs">Active</span>
          </button>
          <button
            onClick={() => setActiveTab('history')}
            className={`flex flex-col items-center py-2 px-3 transition-colors ${
              activeTab === 'history' ? 'text-primary-600' : 'text-gray-400'
            }`}
          >
            <History className="w-6 h-6 mb-1" />
            <span className="text-xs">History</span>
          </button>
          <button
            onClick={() => setActiveTab('profile')}
            className={`flex flex-col items-center py-2 px-3 transition-colors ${
              activeTab === 'profile' ? 'text-primary-600' : 'text-gray-400'
            }`}
          >
            <User className="w-6 h-6 mb-1" />
            <span className="text-xs">Profile</span>
          </button>
        </div>
      </nav>
    </div>
  );
};

export default CustomerInterface;
