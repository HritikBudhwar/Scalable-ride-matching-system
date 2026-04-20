import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { MapPin, Clock, DollarSign, Phone, AlertCircle, CheckCircle, Loader } from 'lucide-react';
import toast from 'react-hot-toast';
import { tripAPI } from '../services/api';

const AvailableTrips = ({ userInfo, onTripAccepted }) => {
  const [availableTrips, setAvailableTrips] = useState([]);
  const [loading, setLoading] = useState(true);
  const [acceptingTripId, setAcceptingTripId] = useState(null);
  const [acceptedTrip, setAcceptedTrip] = useState(null);

  useEffect(() => {
    fetchAvailableTrips();
    // Refresh every 5 seconds
    const interval = setInterval(fetchAvailableTrips, 5000);
    return () => clearInterval(interval);
  }, []);

  const fetchAvailableTrips = async () => {
    try {
      const response = await tripAPI.getAvailableTrips();
      setAvailableTrips(response.data || []);
    } catch (error) {
      console.error('Failed to fetch available trips:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAcceptTrip = async (tripId) => {
    try {
      setAcceptingTripId(tripId);
      const response = await tripAPI.acceptTrip(tripId, userInfo.userId);
      setAcceptedTrip(response.data);
      toast.success('Trip accepted! Customer is heading to pickup location.');
      
      // Remove from available trips
      setAvailableTrips(availableTrips.filter(trip => trip.id !== tripId));

      if (onTripAccepted) {
        onTripAccepted(response.data);
      }
    } catch (error) {
      console.error('Failed to accept trip:', error);
      toast.error(error.response?.data?.message || 'Failed to accept trip');
    } finally {
      setAcceptingTripId(null);
    }
  };

  const handleRejectTrip = async (tripId) => {
    try {
      await tripAPI.rejectTrip(tripId, userInfo.userId);
      toast.success('Trip rejected. Finding another...');
      setAvailableTrips(availableTrips.filter(trip => trip.id !== tripId));
    } catch (error) {
      console.error('Failed to reject trip:', error);
      toast.error(error.response?.data?.message || 'Failed to reject trip');
    }
  };

  // Show accepted trip details
  if (acceptedTrip) {
    return (
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="bg-gradient-to-br from-green-50 to-emerald-50 rounded-2xl shadow-xl p-8 max-w-2xl"
      >
        <div className="text-center mb-8">
          <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4 text-green-600">
            <CheckCircle className="w-8 h-8" />
          </div>
          <h2 className="text-3xl font-bold text-gray-900">Trip Accepted!</h2>
          <p className="text-gray-600 mt-2">Trip ID: #{acceptedTrip.id}</p>
        </div>

        <div className="space-y-6">
          {/* Customer Info */}
          <div className="bg-white p-6 rounded-lg border-l-4 border-blue-500">
            <h3 className="font-semibold text-gray-900 mb-4">Customer Details</h3>
            <div className="space-y-3">
              <div className="flex items-center">
                <span className="text-gray-600 flex-1">Name:</span>
                <span className="font-semibold text-gray-900">{acceptedTrip.customerName || 'Customer'}</span>
              </div>
              <div className="flex items-center">
                <Phone className="w-5 h-5 text-blue-600 mr-3" />
                <span className="font-semibold text-gray-900">Call Customer</span>
              </div>
            </div>
          </div>

          {/* Trip Details */}
          <div className="bg-white p-6 rounded-lg">
            <h3 className="font-semibold text-gray-900 mb-4">Trip Details</h3>
            <div className="space-y-4">
              <div className="flex items-start">
                <MapPin className="w-5 h-5 text-blue-600 mr-3 flex-shrink-0 mt-1" />
                <div>
                  <p className="text-sm text-gray-600">Pickup</p>
                  <p className="font-semibold text-gray-900">{acceptedTrip.source}</p>
                </div>
              </div>
              <div className="border-t pt-4 flex items-start">
                <MapPin className="w-5 h-5 text-green-600 mr-3 flex-shrink-0 mt-1" />
                <div>
                  <p className="text-sm text-gray-600">Destination</p>
                  <p className="font-semibold text-gray-900">{acceptedTrip.destination}</p>
                </div>
              </div>
              <div className="border-t pt-4 flex items-center">
                <Clock className="w-5 h-5 text-amber-600 mr-3" />
                <div>
                  <p className="text-sm text-gray-600">Status</p>
                  <p className="font-semibold text-gray-900 capitalize">{acceptedTrip.status?.toLowerCase()}</p>
                </div>
              </div>
            </div>
          </div>

          {/* Action Buttons */}
          <div className="flex gap-3">
            <button
              onClick={() => setAcceptedTrip(null)}
              className="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-800 font-medium py-3 px-4 rounded-lg transition-colors"
            >
              Back to Trips
            </button>
            <button
              className="flex-1 bg-green-600 hover:bg-green-700 text-white font-medium py-3 px-4 rounded-lg transition-colors"
            >
              Navigate
            </button>
          </div>
        </div>
      </motion.div>
    );
  }

  // Loading state
  if (loading) {
    return (
      <div className="flex items-center justify-center p-8">
        <Loader className="w-8 h-8 animate-spin text-blue-600" />
        <span className="ml-2 text-gray-600">Loading available trips...</span>
      </div>
    );
  }

  // No trips available
  if (availableTrips.length === 0) {
    return (
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="bg-yellow-50 border-l-4 border-yellow-400 p-6 rounded-lg text-center"
      >
        <AlertCircle className="w-12 h-12 text-yellow-600 mx-auto mb-4" />
        <h3 className="font-semibold text-gray-900 mb-2">No trips available right now</h3>
        <p className="text-gray-600 mb-4">
          Check back soon! Refreshing every 5 seconds.
        </p>
        <button
          onClick={fetchAvailableTrips}
          className="bg-yellow-600 hover:bg-yellow-700 text-white font-medium py-2 px-6 rounded-lg transition-colors"
        >
          Refresh Now
        </button>
      </motion.div>
    );
  }

  // Available trips list
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="space-y-4 max-w-2xl"
    >
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold text-gray-900">Available Trips ({availableTrips.length})</h2>
        <button
          onClick={fetchAvailableTrips}
          className="bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded-lg transition-colors text-sm"
        >
          Refresh
        </button>
      </div>

      {availableTrips.map((trip) => (
        <motion.div
          key={trip.id}
          initial={{ opacity: 0, x: -20 }}
          animate={{ opacity: 1, x: 0 }}
          className="bg-white rounded-xl shadow-lg p-6 border-l-4 border-blue-500 hover:shadow-xl transition-shadow"
        >
          <div className="grid md:grid-cols-2 gap-6">
            {/* Trip Details */}
            <div className="space-y-4">
              <div className="flex items-start">
                <MapPin className="w-5 h-5 text-blue-600 mr-3 flex-shrink-0 mt-1" />
                <div>
                  <p className="text-sm text-gray-600">From</p>
                  <p className="font-semibold text-gray-900 text-lg">{trip.source}</p>
                </div>
              </div>

              <div className="flex items-start">
                <MapPin className="w-5 h-5 text-green-600 mr-3 flex-shrink-0 mt-1" />
                <div>
                  <p className="text-sm text-gray-600">To</p>
                  <p className="font-semibold text-gray-900 text-lg">{trip.destination}</p>
                </div>
              </div>

              <div className="flex items-center">
                <DollarSign className="w-5 h-5 text-amber-600 mr-3" />
                <div>
                  <p className="text-sm text-gray-600">Estimated Fare</p>
                  <p className="font-semibold text-gray-900 text-lg">
                    {trip.estimatedFare != null ? `₹${trip.estimatedFare}` : 'Calculating...'}
                  </p>
                </div>
              </div>

              <div className="flex items-center">
                <Clock className="w-5 h-5 text-purple-600 mr-3" />
                <div>
                  <p className="text-sm text-gray-600">Estimated Distance</p>
                  <p className="font-semibold text-gray-900 text-lg">
                    {trip.estimatedDistance != null ? `${trip.estimatedDistance} km` : 'N/A'}
                  </p>
                </div>
              </div>
            </div>

            {/* Actions */}
            <div className="flex flex-col justify-between">
              <div className="bg-blue-50 p-4 rounded-lg mb-4">
                <p className="text-sm text-gray-600">Trip ID</p>
                <p className="font-bold text-blue-600 text-lg">#{trip.id}</p>
              </div>

              <div className="flex gap-3">
                <button
                  onClick={() => handleRejectTrip(trip.id)}
                  className="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-800 font-medium py-2 rounded-lg transition-colors text-sm"
                >
                  Reject
                </button>
                <button
                  onClick={() => handleAcceptTrip(trip.id)}
                  disabled={acceptingTripId === trip.id}
                  className="flex-1 bg-green-600 hover:bg-green-700 text-white font-medium py-2 rounded-lg transition-colors text-sm disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center"
                >
                  {acceptingTripId === trip.id ? (
                    <>
                      <Loader className="w-4 h-4 mr-1 animate-spin" />
                      Accepting...
                    </>
                  ) : (
                    'Accept Trip'
                  )}
                </button>
              </div>
            </div>
          </div>
        </motion.div>
      ))}
    </motion.div>
  );
};

export default AvailableTrips;
