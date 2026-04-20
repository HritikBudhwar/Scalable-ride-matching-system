import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { 
  CreditCard, 
  CheckCircle, 
  ArrowLeft,
  Smartphone,
  Shield,
  IndianRupee
} from 'lucide-react';
import toast from 'react-hot-toast';

const PaymentPage = ({ trip, onPaymentComplete, onBack }) => {
  const [upiId, setUpiId] = useState('');
  const [processing, setProcessing] = useState(false);
  const [paymentDone, setPaymentDone] = useState(false);

  const fareAmount = trip?.invoiceTotal || trip?.estimatedFare || 0;

  const handlePayment = async () => {
    if (!upiId.trim()) {
      toast.error('Please enter your UPI ID');
      return;
    }

    // Basic UPI ID validation (user@bank format)
    const upiRegex = /^[\w.-]+@[\w]+$/;
    if (!upiRegex.test(upiId.trim())) {
      toast.error('Please enter a valid UPI ID (e.g. name@upi)');
      return;
    }

    try {
      setProcessing(true);
      // Simulate UPI payment processing
      await new Promise(resolve => setTimeout(resolve, 2500));
      setPaymentDone(true);
      toast.success('Payment successful!');
    } catch (error) {
      toast.error('Payment failed. Please try again.');
    } finally {
      setProcessing(false);
    }
  };

  if (paymentDone) {
    return (
      <motion.div
        initial={{ opacity: 0, scale: 0.9 }}
        animate={{ opacity: 1, scale: 1 }}
        className="space-y-6"
      >
        <div className="card">
          <div className="p-8 text-center">
            <motion.div
              initial={{ scale: 0 }}
              animate={{ scale: 1 }}
              transition={{ delay: 0.2, type: 'spring', stiffness: 200 }}
              className="w-20 h-20 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-6"
            >
              <CheckCircle className="w-10 h-10 text-green-600" />
            </motion.div>

            <h2 className="text-2xl font-bold text-gray-900 mb-2">Payment Successful!</h2>
            <p className="text-gray-600 mb-6">Your ride payment has been processed via UPI</p>

            <div className="bg-green-50 p-4 rounded-lg mb-6">
              <div className="text-sm text-gray-600">Amount Paid</div>
              <div className="text-3xl font-bold text-green-600">₹{fareAmount}</div>
              <div className="text-sm text-gray-500 mt-1">UPI ID: {upiId}</div>
            </div>

            <div className="bg-gray-50 p-4 rounded-lg mb-6 text-left space-y-2">
              <div className="flex justify-between text-sm">
                <span className="text-gray-600">Trip ID</span>
                <span className="font-medium text-gray-900">#{trip?.id}</span>
              </div>
              <div className="flex justify-between text-sm">
                <span className="text-gray-600">From</span>
                <span className="font-medium text-gray-900">{trip?.source}</span>
              </div>
              <div className="flex justify-between text-sm">
                <span className="text-gray-600">To</span>
                <span className="font-medium text-gray-900">{trip?.destination}</span>
              </div>
              <div className="flex justify-between text-sm">
                <span className="text-gray-600">Driver</span>
                <span className="font-medium text-gray-900">{trip?.driverName || 'N/A'}</span>
              </div>
            </div>

            <button
              onClick={onPaymentComplete}
              className="w-full bg-primary-600 hover:bg-primary-700 text-white font-semibold py-3 px-6 rounded-lg transition-colors"
            >
              Back to Home
            </button>
          </div>
        </div>
      </motion.div>
    );
  }

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="space-y-6"
    >
      {/* Header */}
      <div className="flex items-center space-x-3 mb-2">
        <button
          onClick={onBack}
          className="p-2 hover:bg-gray-100 rounded-lg transition-colors"
        >
          <ArrowLeft className="w-5 h-5 text-gray-600" />
        </button>
        <h2 className="text-xl font-bold text-gray-900">Payment</h2>
      </div>

      {/* Ride Completed Banner */}
      <div className="bg-gradient-to-r from-green-500 to-emerald-600 rounded-xl p-6 text-white">
        <div className="flex items-center space-x-3 mb-3">
          <CheckCircle className="w-6 h-6" />
          <span className="font-semibold text-lg">Ride Completed!</span>
        </div>
        <p className="text-green-100 text-sm">
          Your ride has been completed. Please pay to finish the trip.
        </p>
      </div>

      {/* Trip Summary */}
      <div className="card">
        <div className="p-5">
          <h3 className="font-semibold text-gray-900 mb-4">Trip Summary</h3>
          <div className="space-y-3">
            <div className="flex justify-between text-sm">
              <span className="text-gray-600">Trip ID</span>
              <span className="font-medium text-gray-900">#{trip?.id}</span>
            </div>
            <div className="flex justify-between text-sm">
              <span className="text-gray-600">Driver</span>
              <span className="font-medium text-gray-900">{trip?.driverName || 'N/A'}</span>
            </div>
            <div className="flex justify-between text-sm">
              <span className="text-gray-600">Distance</span>
              <span className="font-medium text-gray-900">
                {trip?.estimatedDistance ? `${trip.estimatedDistance} km` : 'N/A'}
              </span>
            </div>
            <div className="border-t pt-3 flex justify-between">
              <span className="font-semibold text-gray-900">Total Amount</span>
              <span className="text-2xl font-bold text-primary-600">₹{fareAmount}</span>
            </div>
          </div>
        </div>
      </div>

      {/* UPI Payment */}
      <div className="card">
        <div className="p-5">
          <div className="flex items-center space-x-2 mb-4">
            <Smartphone className="w-5 h-5 text-purple-600" />
            <h3 className="font-semibold text-gray-900">Pay via UPI</h3>
          </div>

          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Enter your UPI ID
              </label>
              <input
                type="text"
                placeholder="yourname@upi"
                value={upiId}
                onChange={(e) => setUpiId(e.target.value)}
                className="input-field"
                disabled={processing}
              />
              <p className="text-xs text-gray-500 mt-1">
                e.g. name@paytm, name@gpay, name@phonepe
              </p>
            </div>

            {/* Popular UPI Apps */}
            <div>
              <div className="text-sm text-gray-600 mb-2">Quick Select</div>
              <div className="grid grid-cols-3 gap-2">
                {['@paytm', '@gpay', '@phonepe', '@ybl', '@ibl', '@axl'].map((suffix) => (
                  <button
                    key={suffix}
                    onClick={() => setUpiId(prev => {
                      const name = prev.split('@')[0] || '';
                      return name + suffix;
                    })}
                    className="px-3 py-2 bg-gray-100 hover:bg-gray-200 rounded-lg text-sm text-gray-700 transition-colors"
                  >
                    {suffix}
                  </button>
                ))}
              </div>
            </div>

            <div className="flex items-center space-x-2 text-sm text-gray-500 bg-gray-50 p-3 rounded-lg">
              <Shield className="w-4 h-4 text-green-600 flex-shrink-0" />
              <span>Your payment is secured with end-to-end encryption</span>
            </div>

            <button
              onClick={handlePayment}
              disabled={processing || !upiId.trim()}
              className="w-full bg-purple-600 hover:bg-purple-700 disabled:opacity-50 disabled:cursor-not-allowed text-white font-semibold py-3 px-6 rounded-lg transition-colors flex items-center justify-center"
            >
              {processing ? (
                <>
                  <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white mr-2"></div>
                  Processing Payment...
                </>
              ) : (
                <>
                  <CreditCard className="w-5 h-5 mr-2" />
                  Pay ₹{fareAmount} via UPI
                </>
              )}
            </button>
          </div>
        </div>
      </div>
    </motion.div>
  );
};

export default PaymentPage;
