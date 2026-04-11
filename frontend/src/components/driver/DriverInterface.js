import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { 
  Car, 
  MapPin, 
  Clock, 
  DollarSign, 
  User, 
  Home, 
  History, 
  Settings,
  Phone,
  Navigation,
  TrendingUp,
  Calendar,
  Star,
  ArrowLeft,
  Power,
  CheckCircle,
  XCircle,
  AlertCircle
} from 'lucide-react';
import toast from 'react-hot-toast';
import { driverAPI, tripAPI } from '../../services/api';
import AvailableTrips from '../AvailableTrips';

const DriverInterface = ({ userInfo, onLogout }) => {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('home');
  const [isOnline, setIsOnline] = useState(false);
  const [currentTrip, setCurrentTrip] = useState(null);
  const [driverTrips, setDriverTrips] = useState([]);
  const [availableTrips, setAvailableTrips] = useState([]);
  const [loading, setLoading] = useState(false);
  const driverId = userInfo?.userId;

  // Mock earnings data - would come from backend
  const earningsData = {
    today: 125.50,
    week: 876.25,
    month: 3450.00,
    tripsToday: 8,
    tripsWeek: 52,
    tripsMonth: 198
  };

  // Load driver trips on component mount
  useEffect(() => {
    if (driverId) {
      loadDriverTrips();
    }
  }, []);

  const loadDriverTrips = async () => {
    try {
      setLoading(true);
      const response = await driverAPI.getDriverTrips(driverId);
      setDriverTrips(response.data);
    } catch (error) {
      console.error('Failed to load driver trips:', error);
      toast.error('Failed to load trips');
    } finally {
      setLoading(false);
    }
  };

  const loadAvailableTrips = async () => {
    try {
      setLoading(true);
      const response = await tripAPI.getAvailableTrips();
      setAvailableTrips(response.data || []);
    } catch (error) {
      console.error('Failed to load available trips:', error);
      toast.error('Failed to load available trips');
    } finally {
      setLoading(false);
    }
  };

  // Load available trips when driver goes online
  useEffect(() => {
    if (isOnline) {
      loadAvailableTrips();
    } else {
      setAvailableTrips([]);
    }
  }, [isOnline]);

  const handleAcceptTrip = async (trip) => {
    try {
      setLoading(true);
      await driverAPI.acceptTrip(trip.id, driverId);
      setCurrentTrip(trip);
      setIsOnline(false);
      toast.success(`Trip accepted! Pickup at ${trip.source}`);
      setActiveTab('current');
      // Refresh trips
      loadDriverTrips();
      loadAvailableTrips();
    } catch (error) {
      console.error('Failed to accept trip:', error);
      toast.error('Failed to accept trip');
    } finally {
      setLoading(false);
    }
  };

  const handleCompleteTrip = async () => {
    try {
      setLoading(true);
      await driverAPI.updateTripStatus(currentTrip.id, 'COMPLETED');
      toast.success('Trip completed successfully!');
      setCurrentTrip(null);
      setIsOnline(true);
      setActiveTab('home');
      // Refresh trips
      loadDriverTrips();
    } catch (error) {
      console.error('Failed to complete trip:', error);
      toast.error('Failed to complete trip');
    } finally {
      setLoading(false);
    }
  };

  const handleContactCustomer = () => {
    if (!currentTrip) return;
    const phone = currentTrip.customerPhone;
    if (!phone) {
      toast.error('Customer contact not available yet');
      return;
    }
    window.open(`tel:${phone}`, '_self');
  };

  
  const toggleOnlineStatus = () => {
    setIsOnline(!isOnline);
    toast.success(isOnline ? 'You are now offline' : 'You are now online and ready to accept trips');
  };

  const renderContent = () => {
    switch (activeTab) {
      case 'home':
        return (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="space-y-6"
          >
            {/* Status Card */}
            <div className="card">
              <div className="p-6">
                <div className="flex items-center justify-between mb-6">
                  <h3 className="text-xl font-semibold text-gray-900">Driver Status</h3>
                  <button
                    onClick={toggleOnlineStatus}
                    className={`flex items-center space-x-2 px-4 py-2 rounded-lg font-medium transition-colors ${
                      isOnline 
                        ? 'bg-green-100 text-green-800 hover:bg-green-200' 
                        : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
                    }`}
                  >
                    <div className={`w-2 h-2 rounded-full ${isOnline ? 'bg-green-500' : 'bg-gray-400'}`}></div>
                    <span>{isOnline ? 'Online' : 'Offline'}</span>
                  </button>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div className="text-center p-4 bg-primary-50 rounded-lg">
                    <div className="text-2xl font-bold text-primary-600">{earningsData.tripsToday}</div>
                    <div className="text-sm text-gray-600">Trips Today</div>
                  </div>
                  <div className="text-center p-4 bg-green-50 rounded-lg">
                    <div className="text-2xl font-bold text-green-600">${earningsData.today}</div>
                    <div className="text-sm text-gray-600">Earned Today</div>
                  </div>
                </div>
              </div>
            </div>

            {/* Available Trips */}
            {isOnline && (
              <AvailableTrips 
                userInfo={userInfo} 
                onTripAccepted={(acceptedTrip) => {
                  setCurrentTrip(acceptedTrip);
                  setIsOnline(false);
                  setActiveTab('current');
                }}
              />
            )}

            {!isOnline && (
              <div className="text-center py-12">
                <Car className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                <h3 className="text-lg font-medium text-gray-900 mb-2">You're Offline</h3>
                <p className="text-gray-600 mb-4">Go online to start receiving trip requests</p>
                <button
                  onClick={toggleOnlineStatus}
                  className="btn-primary"
                >
                  Go Online
                </button>
              </div>
            )}

            {/* Quick Stats */}
            <div className="grid grid-cols-2 gap-4">
              <div className="card p-4">
                <div className="flex items-center space-x-3">
                  <div className="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center">
                    <TrendingUp className="w-5 h-5 text-blue-600" />
                  </div>
                  <div>
                    <div className="text-sm text-gray-600">This Week</div>
                    <div className="font-semibold text-gray-900">${earningsData.week}</div>
                  </div>
                </div>
              </div>
              <div className="card p-4">
                <div className="flex items-center space-x-3">
                  <div className="w-10 h-10 bg-purple-100 rounded-full flex items-center justify-center">
                    <Calendar className="w-5 h-5 text-purple-600" />
                  </div>
                  <div>
                    <div className="text-sm text-gray-600">This Month</div>
                    <div className="font-semibold text-gray-900">${earningsData.month}</div>
                  </div>
                </div>
              </div>
            </div>
          </motion.div>
        );

      case 'current':
        return (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="space-y-6"
          >
            {currentTrip ? (
              <div className="card">
                <div className="p-6">
                  <div className="flex items-center justify-between mb-6">
                    <h3 className="text-xl font-semibold text-gray-900">Current Trip</h3>
                    <span className="px-3 py-1 bg-blue-100 text-blue-800 rounded-full text-sm font-medium">
                      In Progress
                    </span>
                  </div>

                  <div className="space-y-4">
                    <div className="bg-blue-50 p-4 rounded-lg">
                      <div className="flex items-center justify-between mb-2">
                        <span className="font-medium text-gray-900">Customer</span>
                        <span className="text-gray-600">{currentTrip.customerName || 'Customer'}</span>
                      </div>
                      <div className="flex items-center space-x-1 text-sm text-gray-600">
                        <Star className="w-4 h-4 text-yellow-400 fill-current" />
                        <span>{currentTrip.customerRating || 'N/A'} rating</span>
                      </div>
                    </div>

                    <div className="space-y-3">
                      <div className="flex items-center space-x-3">
                        <div className="w-10 h-10 bg-green-100 rounded-full flex items-center justify-center">
                          <MapPin className="w-5 h-5 text-green-600" />
                        </div>
                        <div className="flex-1">
                          <div className="text-sm text-gray-600">Pickup</div>
                          <div className="font-medium text-gray-900">{currentTrip.source}</div>
                        </div>
                      </div>

                      <div className="flex items-center space-x-3">
                        <div className="w-10 h-10 bg-red-100 rounded-full flex items-center justify-center">
                          <Navigation className="w-5 h-5 text-red-600" />
                        </div>
                        <div className="flex-1">
                          <div className="text-sm text-gray-600">Destination</div>
                          <div className="font-medium text-gray-900">{currentTrip.destination}</div>
                        </div>
                      </div>
                    </div>

                    <div className="grid grid-cols-2 gap-4 p-4 bg-gray-50 rounded-lg">
                      <div>
                        <div className="text-sm text-gray-600">Distance</div>
                        <div className="text-lg font-semibold text-gray-900">
                          {currentTrip.distanceTraveled != null ? `${currentTrip.distanceTraveled} km` : 'N/A'}
                        </div>
                      </div>
                      <div>
                        <div className="text-sm text-gray-600">Estimated Fare</div>
                        <div className="text-lg font-semibold text-gray-900">
                          {currentTrip.invoiceTotal != null ? `₹${currentTrip.invoiceTotal}` : 'N/A'}
                        </div>
                      </div>
                    </div>

                    <div className="flex space-x-3">
                      <button
                        onClick={handleContactCustomer}
                        className="flex-1 bg-yellow-600 hover:bg-yellow-700 text-white font-medium py-3 px-4 rounded-lg transition-colors flex items-center justify-center"
                      >
                        <Phone className="w-5 h-5 mr-2" />
                        Contact Customer
                      </button>
                      <button
                        onClick={handleCompleteTrip}
                        className="flex-1 bg-green-600 hover:bg-green-700 text-white font-medium py-3 px-4 rounded-lg transition-colors flex items-center justify-center"
                      >
                        <CheckCircle className="w-5 h-5 mr-2" />
                        Complete Trip
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            ) : (
              <div className="text-center py-12">
                <Car className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                <h3 className="text-lg font-medium text-gray-900 mb-2">No Active Trip</h3>
                <p className="text-gray-600 mb-4">You don't have any active trips</p>
                <button
                  onClick={() => setActiveTab('home')}
                  className="btn-primary"
                >
                  View Available Trips
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
            <h3 className="text-xl font-semibold text-gray-900 mb-4">Trip History</h3>
            {loading ? (
              <div className="text-center py-8">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600 mx-auto"></div>
                <p className="text-gray-600 mt-2">Loading trips...</p>
              </div>
            ) : driverTrips.length === 0 ? (
              <div className="text-center py-8">
                <Car className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                <h3 className="text-lg font-medium text-gray-900 mb-2">No trips found</h3>
                <p className="text-gray-600">You haven't completed any trips yet</p>
              </div>
            ) : (
              driverTrips.map((trip) => (
                <div key={trip.id} className="card">
                  <div className="p-4">
                    <div className="flex items-center justify-between mb-3">
                      <div>
                        <div className="font-medium text-gray-900">
                          {trip.source} to {trip.destination}
                        </div>
                        <div className="text-sm text-gray-600">
                          {trip.startTime ? new Date(trip.startTime).toLocaleDateString() : 'N/A'} at 
                          {trip.startTime ? new Date(trip.startTime).toLocaleTimeString() : 'N/A'}
                        </div>
                      </div>
                      <div className="text-right">
                        <div className="font-semibold text-gray-900">
                          ${trip.invoiceTotal || 'N/A'}
                        </div>
                        <div className="flex items-center space-x-1">
                          {[...Array(5)].map((_, i) => (
                            <Star
                              key={i}
                              className={`w-4 h-4 ${
                                i < 4 ? 'text-yellow-400 fill-current' : 'text-gray-300'
                              }`}
                            />
                          ))}
                        </div>
                      </div>
                    </div>
                    
                    <div className="space-y-2 text-sm text-gray-600">
                      <div className="flex items-center justify-between">
                        <span>Distance: {trip.distanceTraveled || 'N/A'} km</span>
                        <span>Duration: {trip.startTime && trip.endTime ? 
                          Math.round((new Date(trip.endTime) - new Date(trip.startTime)) / 60000) + ' min' : 'N/A'}
                        </span>
                      </div>
                      {trip.driverName && (
                        <div className="flex items-center justify-between">
                          <span>Driver: {trip.driverName}</span>
                          <span>Vehicle: {trip.vehicleModel || 'N/A'}</span>
                        </div>
                      )}
                    </div>

                    <div className="mt-3 flex items-center justify-between">
                      <span className={`px-2 py-1 rounded-full text-xs ${
                        trip.tripStatus === 'COMPLETED' ? 'bg-green-100 text-green-800' :
                        trip.tripStatus === 'IN_PROGRESS' ? 'bg-blue-100 text-blue-800' :
                        'bg-red-100 text-red-800'
                      }`}>
                        {trip.tripStatus || 'UNKNOWN'}
                      </span>
                      <button className="text-primary-600 hover:text-primary-700 text-sm font-medium">
                        View Details
                      </button>
                    </div>
                  </div>
                </div>
              ))
            )}
          </motion.div>
        );

      case 'earnings':
        return (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="space-y-6"
          >
            <h3 className="text-xl font-semibold text-gray-900 mb-4">Earnings Overview</h3>
            
            <div className="grid grid-cols-2 gap-4">
              <div className="card p-4">
                <div className="text-center">
                  <div className="text-3xl font-bold text-primary-600">${earningsData.today}</div>
                  <div className="text-sm text-gray-600">Today's Earnings</div>
                  <div className="text-xs text-gray-500 mt-1">{earningsData.tripsToday} trips</div>
                </div>
              </div>
              <div className="card p-4">
                <div className="text-center">
                  <div className="text-3xl font-bold text-green-600">${earningsData.week}</div>
                  <div className="text-sm text-gray-600">This Week</div>
                  <div className="text-xs text-gray-500 mt-1">{earningsData.tripsWeek} trips</div>
                </div>
              </div>
              <div className="card p-4">
                <div className="text-center">
                  <div className="text-3xl font-bold text-purple-600">${earningsData.month}</div>
                  <div className="text-sm text-gray-600">This Month</div>
                  <div className="text-xs text-gray-500 mt-1">{earningsData.tripsMonth} trips</div>
                </div>
              </div>
              <div className="card p-4">
                <div className="text-center">
                  <div className="text-3xl font-bold text-orange-600">
                    ${(earningsData.month / earningsData.tripsMonth).toFixed(2)}
                  </div>
                  <div className="text-sm text-gray-600">Avg per Trip</div>
                  <div className="text-xs text-gray-500 mt-1">Monthly average</div>
                </div>
              </div>
            </div>

            <div className="card">
              <div className="p-4">
                <h4 className="font-medium text-gray-900 mb-3">Payment Summary</h4>
                <div className="space-y-2">
                  <div className="flex justify-between text-sm">
                    <span className="text-gray-600">Total Earnings</span>
                    <span className="font-medium text-gray-900">${earningsData.month}</span>
                  </div>
                  <div className="flex justify-between text-sm">
                    <span className="text-gray-600">Platform Fee (10%)</span>
                    <span className="font-medium text-red-600">-${(earningsData.month * 0.1).toFixed(2)}</span>
                  </div>
                  <div className="border-t pt-2 flex justify-between">
                    <span className="font-medium text-gray-900">Net Earnings</span>
                    <span className="font-bold text-green-600">${(earningsData.month * 0.9).toFixed(2)}</span>
                  </div>
                </div>
              </div>
            </div>
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
                <h3 className="text-xl font-semibold text-gray-900 mb-6">Driver Profile</h3>
                
                <div className="space-y-4">
                  <div className="flex items-center space-x-4">
                    <div className="w-20 h-20 bg-primary-100 rounded-full flex items-center justify-center">
                      <User className="w-10 h-10 text-primary-600" />
                    </div>
                    <div>
                      <div className="font-medium text-gray-900">
                        {userInfo?.firstName} {userInfo?.lastName}
                      </div>
                      <div className="text-sm text-gray-600">Professional Driver</div>
                      <div className="flex items-center space-x-1 mt-1">
                        <Star className="w-4 h-4 text-yellow-400 fill-current" />
                        <span className="text-sm text-gray-600">4.8 Rating</span>
                      </div>
                    </div>
                  </div>

                  <div className="space-y-3">
                    <div className="flex items-center space-x-3">
                      <Car className="w-5 h-5 text-gray-400" />
                      <span className="text-gray-700">
                        {currentTrip?.vehicleModel ? `${currentTrip.vehicleModel} · ${currentTrip.vehicleReg || ''}` : 'Vehicle: N/A'}
                      </span>
                    </div>
                    <div className="flex items-center space-x-3">
                      <Phone className="w-5 h-5 text-gray-400" />
                      <span className="text-gray-700">{userInfo?.phone || 'Phone: N/A'}</span>
                    </div>
                    <div className="flex items-center space-x-3">
                      <AlertCircle className="w-5 h-5 text-green-500" />
                      <span className="text-gray-700">Verified Driver</span>
                    </div>
                  </div>

                  <div className="grid grid-cols-2 gap-3">
                    <button className="btn-secondary">Edit Profile</button>
                    <button className="btn-secondary">Vehicle Info</button>
                  </div>
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
              <h1 className="text-xl font-bold text-gray-900">Driver Portal</h1>
            </div>
            <div className="flex items-center space-x-3">
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
        <div className="grid grid-cols-5 py-2">
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
            onClick={() => setActiveTab('current')}
            className={`flex flex-col items-center py-2 px-3 transition-colors ${
              activeTab === 'current' ? 'text-primary-600' : 'text-gray-400'
            }`}
          >
            <Car className="w-6 h-6 mb-1" />
            <span className="text-xs">Current</span>
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
            onClick={() => setActiveTab('earnings')}
            className={`flex flex-col items-center py-2 px-3 transition-colors ${
              activeTab === 'earnings' ? 'text-primary-600' : 'text-gray-400'
            }`}
          >
            <DollarSign className="w-6 h-6 mb-1" />
            <span className="text-xs">Earnings</span>
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

export default DriverInterface;
