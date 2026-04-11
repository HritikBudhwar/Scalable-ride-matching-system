import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { MapPin, DollarSign, AlertCircle, CheckCircle, Loader } from 'lucide-react';
import toast from 'react-hot-toast';
import { bookingAPI, tripAPI } from '../services/api';

const BookingPage = ({ userInfo, onBookingCreated }) => {
  const [bookingData, setBookingData] = useState({
    source: '',
    destination: '',
    serviceType: 'STANDARD'
  });

  const [loading, setLoading] = useState(false);
  const [bookingId, setBookingId] = useState(null);
  const [tripId, setTripId] = useState(null);
  const [errors, setErrors] = useState({});

  const validateForm = () => {
    const newErrors = {};
    if (!bookingData.source.trim()) newErrors.source = 'Pickup location is required';
    if (!bookingData.destination.trim()) newErrors.destination = 'Destination is required';
    if (bookingData.source === bookingData.destination) newErrors.destination = 'Pickup and destination must be different';
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleCreateBooking = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      toast.error('Please fill in all fields correctly');
      return;
    }

    try {
      setLoading(true);

      // Create booking
      const bookingResponse = await bookingAPI.createBooking(bookingData, userInfo.userId);
      setBookingId(bookingResponse.data.id);
      toast.success('Booking created! Estimating fare...');

    } catch (error) {
      console.error('Booking failed:', error);
      const errorMessage = error.response?.data?.message || 'Failed to create booking';
      toast.error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  const handleConfirmBooking = async () => {
    try {
      setLoading(true);
      const tripResponse = await bookingAPI.confirmBooking(bookingId);
      setTripId(tripResponse.data.id);
      toast.success('Booking confirmed! Finding nearby drivers...');
      
      if (onBookingCreated) {
        onBookingCreated(tripResponse.data);
      }
    } catch (error) {
      console.error('Confirmation failed:', error);
      toast.error('Failed to confirm booking');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setBookingData(prev => ({
      ...prev,
      [name]: value
    }));
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  // Show trip tracking if confirmed
  if (tripId) {
    return (
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="bg-white rounded-2xl shadow-xl p-8 max-w-2xl"
      >
        <div className="text-center mb-8">
          <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4 text-green-600">
            <CheckCircle className="w-8 h-8" />
          </div>
          <h2 className="text-3xl font-bold text-gray-900">Trip Confirmed!</h2>
          <p className="text-gray-600 mt-2">Your booking ID: #{tripId}</p>
        </div>

        <div className="space-y-6">
          <div className="bg-blue-50 p-6 rounded-lg border-l-4 border-blue-500">
            <h3 className="font-semibold text-gray-900 mb-4">Trip Details</h3>
            <div className="space-y-3">
              <div className="flex items-start">
                <MapPin className="w-5 h-5 text-blue-600 mr-3 flex-shrink-0 mt-1" />
                <div>
                  <p className="text-sm text-gray-600">From</p>
                  <p className="font-semibold text-gray-900">{bookingData.source}</p>
                </div>
              </div>
              <div className="flex items-start">
                <MapPin className="w-5 h-5 text-green-600 mr-3 flex-shrink-0 mt-1" />
                <div>
                  <p className="text-sm text-gray-600">To</p>
                  <p className="font-semibold text-gray-900">{bookingData.destination}</p>
                </div>
              </div>
              <div className="flex items-start">
                <DollarSign className="w-5 h-5 text-amber-600 mr-3 flex-shrink-0 mt-1" />
                <div>
                  <p className="text-sm text-gray-600">Service Type</p>
                  <p className="font-semibold text-gray-900 capitalize">{bookingData.serviceType.toLowerCase()}</p>
                </div>
              </div>
            </div>
          </div>

          <div className="bg-yellow-50 border-l-4 border-yellow-400 p-4 rounded">
            <div className="flex items-start">
              <AlertCircle className="w-5 h-5 text-yellow-600 mr-3 flex-shrink-0 mt-0.5" />
              <div>
                <p className="font-semibold text-yellow-800">Finding nearby drivers...</p>
                <p className="text-sm text-yellow-700 mt-1">
                  Please wait while we search for available drivers in your area. You'll be notified when a driver accepts.
                </p>
              </div>
            </div>
          </div>

          <button
            onClick={() => {
              setTripId(null);
              setBookingId(null);
              setBookingData({ source: '', destination: '', serviceType: 'STANDARD' });
            }}
            className="w-full bg-blue-600 hover:bg-blue-700 text-white font-medium py-3 px-4 rounded-lg transition-colors"
          >
            Book Another Ride
          </button>
        </div>
      </motion.div>
    );
  }

  // Show fare estimate if booking created
  if (bookingId) {
    return (
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="bg-white rounded-2xl shadow-xl p-8 max-w-2xl"
      >
        <div className="text-center mb-8">
          <h2 className="text-3xl font-bold text-gray-900">Confirm Your Booking</h2>
          <p className="text-gray-600 mt-2">Review and confirm your trip details</p>
        </div>

        <div className="space-y-6">
          <div className="bg-gray-50 p-6 rounded-lg space-y-4">
            <div className="flex justify-between items-center pb-4 border-b">
              <span className="text-gray-600">From</span>
              <span className="font-semibold text-gray-900">{bookingData.source}</span>
            </div>
            <div className="flex justify-between items-center pb-4 border-b">
              <span className="text-gray-600">To</span>
              <span className="font-semibold text-gray-900">{bookingData.destination}</span>
            </div>
            <div className="flex justify-between items-center pb-4 border-b">
              <span className="text-gray-600">Service Type</span>
              <span className="font-semibold text-gray-900 capitalize">{bookingData.serviceType.toLowerCase()}</span>
            </div>
            <div className="flex justify-between items-center pt-2">
              <span className="text-gray-600 font-semibold">Estimated Fare</span>
              <span className="text-2xl font-bold text-green-600">₹299</span>
            </div>
          </div>

          <div className="flex gap-3">
            <button
              onClick={() => {
                setBookingId(null);
                setBookingData({ source: '', destination: '', serviceType: 'STANDARD' });
              }}
              className="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-800 font-medium py-3 px-4 rounded-lg transition-colors"
            >
              Cancel
            </button>
            <button
              onClick={handleConfirmBooking}
              disabled={loading}
              className="flex-1 bg-green-600 hover:bg-green-700 text-white font-medium py-3 px-4 rounded-lg transition-colors disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center"
            >
              {loading ? (
                <>
                  <Loader className="w-5 h-5 mr-2 animate-spin" />
                  Confirming...
                </>
              ) : (
                'Confirm Booking'
              )}
            </button>
          </div>
        </div>
      </motion.div>
    );
  }

  // Show booking form
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="bg-white rounded-2xl shadow-xl p-8 max-w-2xl"
    >
      <div className="text-center mb-8">
        <h2 className="text-3xl font-bold text-gray-900">Book a Ride</h2>
        <p className="text-gray-600 mt-2">Enter your pickup and destination</p>
      </div>

      <form onSubmit={handleCreateBooking} className="space-y-6">
        {/* Pickup Location */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Pickup Location *
          </label>
          <div className="relative">
            <MapPin className="absolute left-3 top-3 w-5 h-5 text-gray-400" />
            <input
              type="text"
              name="source"
              value={bookingData.source}
              onChange={handleChange}
              className={`w-full pl-10 pr-3 py-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 ${
                errors.source ? 'border-red-500' : 'border-gray-300'
              }`}
              placeholder="Enter pickup address"
            />
          </div>
          {errors.source && (
            <p className="mt-1 text-sm text-red-500 flex items-center">
              <AlertCircle className="w-4 h-4 mr-1" /> {errors.source}
            </p>
          )}
        </div>

        {/* Destination */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Destination *
          </label>
          <div className="relative">
            <MapPin className="absolute left-3 top-3 w-5 h-5 text-gray-400" />
            <input
              type="text"
              name="destination"
              value={bookingData.destination}
              onChange={handleChange}
              className={`w-full pl-10 pr-3 py-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 ${
                errors.destination ? 'border-red-500' : 'border-gray-300'
              }`}
              placeholder="Enter destination address"
            />
          </div>
          {errors.destination && (
            <p className="mt-1 text-sm text-red-500 flex items-center">
              <AlertCircle className="w-4 h-4 mr-1" /> {errors.destination}
            </p>
          )}
        </div>

        {/* Service Type */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Service Type
          </label>
          <select
            name="serviceType"
            value={bookingData.serviceType}
            onChange={handleChange}
            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="STANDARD">Standard - Affordable</option>
            <option value="PREMIUM">Premium - Comfort</option>
            <option value="SHARED">Shared - Economy</option>
          </select>
        </div>

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-blue-600 hover:bg-blue-700 text-white font-medium py-3 px-4 rounded-lg transition-colors disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center"
        >
          {loading ? (
            <>
              <Loader className="w-5 h-5 mr-2 animate-spin" />
              Creating Booking...
            </>
          ) : (
            'Get Fare Estimate'
          )}
        </button>
      </form>

      <p className="text-center text-gray-600 text-sm mt-6">
        Estimated fare shown after you enter your destination
      </p>
    </motion.div>
  );
};

export default BookingPage;
