import axios from 'axios';

// Create axios instance with base configuration
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle common errors
api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response?.status === 401) {
      // Handle unauthorized access
      localStorage.removeItem('authToken');
      window.location.href = '/';
    }
    return Promise.reject(error);
  }
);

// Authentication API endpoints
export const authAPI = {
  login: (credentials) => api.post('/auth/login', credentials),
  logout: (token) => api.post('/auth/logout', null, { 
    headers: { Authorization: `Bearer ${token}` }
  }),
  validateToken: (token) => api.get('/auth/validate', { 
    headers: { Authorization: `Bearer ${token}` }
  }),
};

// User API endpoints
export const userAPI = {
  // Customer endpoints
  registerCustomer: (userData) => api.post('/users/customer', userData),
  
  // Driver endpoints
  registerDriver: (userData) => api.post('/users/driver', userData),

  // Admin endpoints
  registerAdmin: (userData) => api.post('/users/admin', userData),
  
  // Authentication endpoints
  sendLoginOTP: (phone) => api.post('/users/login/otp', null, { params: { phone } }),
  login: (phone, otp) => api.post('/users/login', null, { params: { phone, otp } }),
  
  // User profile
  getUserById: (id) => api.get(`/users/${id}`),
};

// Booking API endpoints
export const bookingAPI = {
  // Create booking
  createBooking: (bookingData, customerId) => 
    api.post('/bookings', bookingData, { params: { customerId } }),
  
  // Confirm booking
  confirmBooking: (bookingId) => api.post(`/bookings/${bookingId}/confirm`),
  
  // Cancel booking
  cancelBooking: (bookingId, reason) => 
    api.post(`/bookings/${bookingId}/cancel`, { reason }),
  
  // Get customer bookings
  getCustomerBookings: (customerId) => 
    api.get('/bookings', { params: { customerId } }),
  
  // Get booking by ID
  getBookingById: (bookingId) => api.get(`/bookings/${bookingId}`),
};

// Trip API endpoints
export const tripAPI = {
  // Get trip by ID
  getTripById: (tripId) => api.get(`/trips/${tripId}`),
  
  // Start trip
  startTrip: (tripId) => api.post(`/trips/${tripId}/start`),
  
  // Complete trip
  completeTrip: (tripId) => api.post(`/trips/${tripId}/complete`),
  
  // Cancel trip
  cancelTrip: (tripId, reason) => 
    api.post(`/trips/${tripId}/cancel`, { reason }),
  
  // Get driver trips
  getDriverTrips: (driverId) => 
    api.get('/trips', { params: { driverId } }),
  
  // Update trip location
  updateTripLocation: (tripId, location) => 
    api.put(`/trips/${tripId}/location`, location),
  
  // Get available trips for drivers
  getAvailableTrips: () => api.get('/trips/available'),
  
  // Accept trip
  acceptTrip: (tripId, driverId) => 
    api.post(`/trips/${tripId}/accept`, null, { params: { driverId } }),
  
  // Reject trip
  rejectTrip: (tripId, driverId) => 
    api.post(`/trips/${tripId}/reject`, null, { params: { driverId } }),
};

// Payment API endpoints
export const paymentAPI = {
  // Process payment
  processPayment: (paymentData) => api.post('/payments/process', paymentData),
  
  // Get payment history
  getPaymentHistory: (userId) => 
    api.get('/payments/history', { params: { userId } }),
  
  // Get invoice by ID
  getInvoiceById: (invoiceId) => api.get(`/payments/invoices/${invoiceId}`),
  
  // Refund payment
  refundPayment: (paymentId, reason) => 
    api.post(`/payments/${paymentId}/refund`, { reason }),
};

// Admin API endpoints
export const adminAPI = {
  // Driver management
  getPendingDrivers: () => api.get('/admin/drivers/pending'),
  approveDriver: (driverId) => api.post(`/admin/drivers/${driverId}/approve`),
  rejectDriver: (driverId, reason) => api.post(`/admin/drivers/${driverId}/reject`, { reason }),
  reinstateDriver: (driverId) => api.post(`/admin/drivers/${driverId}/reinstate`),
  
  // User management
  suspendUser: (userId, lifetimeBan = false) => 
    api.post(`/admin/users/${userId}/suspend`, { lifetimeBan }),
  
  // Refund management
  issueRefund: (invoiceId, reason) => 
    api.post(`/admin/invoices/${invoiceId}/refund`, { reason }),
  
  // System statistics
  getSystemStats: () => api.get('/admin/stats'),
};

// Driver API endpoints
export const driverAPI = {
  // Trip management using TripController endpoints
  acceptTrip: (tripId, driverId) => api.post(`/trips/${tripId}/accept`, null, { params: { driverId } }),
  rejectTrip: (tripId, driverId) => api.post(`/trips/${tripId}/reject`, null, { params: { driverId } }),
  updateTripStatus: (tripId, status) => api.put(`/trips/${tripId}/status`, { newStatus: status }),
  updateLocation: (tripId, geoPoint) => api.put(`/trips/${tripId}/location`, { geoPoint }),
  completeParcel: (tripId, otp) => api.post(`/trips/${tripId}/complete-parcel`, { otp }),
  
  // Get driver trips
  getDriverTrips: (driverId) => api.get('/trips', { params: { driverId } }),
  getTripById: (tripId) => api.get(`/trips/${tripId}`),
  
  // Get available trips for drivers (pulling from backend)
  getAvailableTrips: () => api.get('/trips/available'),
};

// Customer API endpoints
export const customerAPI = {
  // Booking management using BookingController endpoints
  createBooking: (bookingData, customerId) => 
    api.post('/bookings', bookingData, { params: { customerId } }),
  confirmBooking: (bookingId) => api.post(`/bookings/${bookingId}/confirm`),
  cancelBooking: (bookingId, reason) => api.post(`/bookings/${bookingId}/cancel`, { reason }),
  getCustomerBookings: (customerId) => api.get('/bookings', { params: { customerId } }),
  getBookingById: (bookingId) => api.get(`/bookings/${bookingId}`),
  
  // Trip history using TripController
  getTripHistory: (customerId) => api.get('/trips', { params: { customerId } }),
  getTripById: (tripId) => api.get(`/trips/${tripId}`),
};

export default api;
