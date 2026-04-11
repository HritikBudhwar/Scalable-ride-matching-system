import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { 
  Shield, 
  Users, 
  Car, 
  TrendingUp, 
  Settings, 
  Home, 
  BarChart3,
  AlertTriangle,
  CheckCircle,
  XCircle,
  Clock,
  DollarSign,
  MapPin,
  Phone,
  Mail,
  Search,
  Filter,
  Download,
  Eye,
  Edit,
  Trash2,
  Plus,
  ArrowLeft,
  Database,
  Headphones,
  FileText,
  Calendar
} from 'lucide-react';
import toast from 'react-hot-toast';
import { adminAPI, tripAPI, userAPI } from '../../services/api';

const AdminInterface = ({ userInfo, onLogout }) => {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('dashboard');
  const [systemStats, setSystemStats] = useState({});
  const [pendingDrivers, setPendingDrivers] = useState([]);
  const [allUsers, setAllUsers] = useState([]);
  const [allTrips, setAllTrips] = useState([]);
  const [loading, setLoading] = useState(false);

  // Load admin data on component mount
  useEffect(() => {
    loadSystemStats();
    loadPendingDrivers();
    loadAllUsers();
    loadAllTrips();
  }, []);

  const loadSystemStats = async () => {
    try {
      setLoading(true);
      const response = await adminAPI.getSystemStats();
      setSystemStats(response.data);
    } catch (error) {
      console.error('Failed to load system stats:', error);
      toast.error('Failed to load system statistics');
    } finally {
      setLoading(false);
    }
  };

  const loadPendingDrivers = async () => {
    try {
      setLoading(true);
      const response = await adminAPI.getPendingDrivers();
      setPendingDrivers(response.data);
    } catch (error) {
      console.error('Failed to load pending drivers:', error);
      toast.error('Failed to load pending drivers');
    } finally {
      setLoading(false);
    }
  };

  const loadAllUsers = async () => {
    try {
      setLoading(true);
      // Mock data - would come from a real endpoint
      const mockUsers = [
        { id: 1, name: 'John Doe', type: 'Customer', email: 'john@example.com', phone: '+1234567890', status: 'Active', joinDate: '2024-01-15' },
        { id: 2, name: 'Jane Smith', type: 'Driver', email: 'jane@example.com', phone: '+0987654321', status: 'Active', joinDate: '2024-01-14' },
        { id: 3, name: 'Bob Johnson', type: 'Customer', email: 'bob@example.com', phone: '+1122334455', status: 'Inactive', joinDate: '2024-01-13' }
      ];
      setAllUsers(mockUsers);
    } catch (error) {
      console.error('Failed to load users:', error);
      toast.error('Failed to load users');
    } finally {
      setLoading(false);
    }
  };

  const loadAllTrips = async () => {
    try {
      setLoading(true);
      // Mock data - would come from a real endpoint
      const mockTrips = [
        { id: 1, customer: 'Alice Brown', driver: 'Mike Wilson', pickup: '123 Main St', destination: 'Airport', fare: '$25.50', status: 'Completed', date: '2024-01-15 14:30' },
        { id: 2, customer: 'Charlie Davis', driver: 'Sarah Lee', pickup: '456 Oak Ave', destination: 'Mall', fare: '$12.00', status: 'In Progress', date: '2024-01-15 15:45' },
        { id: 3, customer: 'Diana Miller', driver: 'Tom Harris', pickup: '789 Pine Rd', destination: 'Station', fare: '$18.75', status: 'Cancelled', date: '2024-01-15 16:20' }
      ];
      setAllTrips(mockTrips);
    } catch (error) {
      console.error('Failed to load trips:', error);
      toast.error('Failed to load trips');
    } finally {
      setLoading(false);
    }
  };

  // Mock stats for now - would come from backend
  const stats = {
    totalUsers: systemStats.totalUsers || 15420,
    activeDrivers: systemStats.activeDrivers || 892,
    totalTrips: systemStats.totalTrips || 45678,
    todayTrips: systemStats.todayTrips || 1247,
    totalRevenue: systemStats.totalRevenue || 125678.90,
    todayRevenue: systemStats.todayRevenue || 3456.78,
    avgRating: systemStats.avgRating || 4.6,
    supportTickets: systemStats.supportTickets || 23
  };

  const recentUsers = [
    { id: 1, name: 'John Doe', type: 'Customer', email: 'john@example.com', phone: '+1234567890', status: 'Active', joinDate: '2024-01-15' },
    { id: 2, name: 'Jane Smith', type: 'Driver', email: 'jane@example.com', phone: '+0987654321', status: 'Active', joinDate: '2024-01-14' },
    { id: 3, name: 'Bob Johnson', type: 'Customer', email: 'bob@example.com', phone: '+1122334455', status: 'Inactive', joinDate: '2024-01-13' }
  ];

  const recentTrips = [
    { id: 1, customer: 'Alice Brown', driver: 'Mike Wilson', pickup: '123 Main St', destination: 'Airport', fare: '$25.50', status: 'Completed', date: '2024-01-15 14:30' },
    { id: 2, customer: 'Charlie Davis', driver: 'Sarah Lee', pickup: '456 Oak Ave', destination: 'Mall', fare: '$12.00', status: 'In Progress', date: '2024-01-15 15:45' },
    { id: 3, customer: 'Diana Miller', driver: 'Tom Harris', pickup: '789 Pine Rd', destination: 'Station', fare: '$18.75', status: 'Cancelled', date: '2024-01-15 16:20' }
  ];

  const supportTickets = [
    { id: 1, user: 'Customer User', subject: 'Payment Issue', priority: 'High', status: 'Open', date: '2024-01-15 10:30' },
    { id: 2, user: 'Driver User', subject: 'App Not Working', priority: 'Medium', status: 'In Progress', date: '2024-01-15 11:15' },
    { id: 3, user: 'Customer User', subject: 'Driver Behavior', priority: 'High', status: 'Resolved', date: '2024-01-15 09:45' }
  ];

  const systemAlerts = [
    { id: 1, type: 'warning', message: 'Server response time increased by 20%', time: '5 minutes ago' },
    { id: 2, type: 'error', message: 'Database connection failed in region US-East', time: '15 minutes ago' },
    { id: 3, type: 'info', message: 'New driver registration spike detected', time: '1 hour ago' }
  ];

  // Admin action functions
  const handleApproveDriver = async (driverId) => {
    try {
      setLoading(true);
      await adminAPI.approveDriver(driverId);
      toast.success('Driver approved successfully');
      loadPendingDrivers();
    } catch (error) {
      console.error('Failed to approve driver:', error);
      toast.error('Failed to approve driver');
    } finally {
      setLoading(false);
    }
  };

  const handleRejectDriver = async (driverId, reason) => {
    try {
      setLoading(true);
      await adminAPI.rejectDriver(driverId, reason);
      toast.success('Driver rejected');
      loadPendingDrivers();
    } catch (error) {
      console.error('Failed to reject driver:', error);
      toast.error('Failed to reject driver');
    } finally {
      setLoading(false);
    }
  };

  const handleSuspendUser = async (userId, lifetimeBan = false) => {
    try {
      setLoading(true);
      await adminAPI.suspendUser(userId, lifetimeBan);
      toast.success('User suspended');
      loadAllUsers();
    } catch (error) {
      console.error('Failed to suspend user:', error);
      toast.error('Failed to suspend user');
    } finally {
      setLoading(false);
    }
  };

  const handleIssueRefund = async (invoiceId, reason) => {
    try {
      setLoading(true);
      await adminAPI.issueRefund(invoiceId, reason);
      toast.success('Refund issued successfully');
    } catch (error) {
      console.error('Failed to issue refund:', error);
      toast.error('Failed to issue refund');
    } finally {
      setLoading(false);
    }
  };

  const renderContent = () => {
    switch (activeTab) {
      case 'dashboard':
        return (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="space-y-6"
          >
            {/* Stats Grid */}
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
              <div className="card p-4">
                <div className="flex items-center justify-between mb-2">
                  <Users className="w-8 h-8 text-blue-500" />
                  <span className="text-xs text-green-600 bg-green-100 px-2 py-1 rounded-full">+12%</span>
                </div>
                <div className="text-2xl font-bold text-gray-900">{stats.totalUsers.toLocaleString()}</div>
                <div className="text-sm text-gray-600">Total Users</div>
              </div>
              
              <div className="card p-4">
                <div className="flex items-center justify-between mb-2">
                  <Car className="w-8 h-8 text-green-500" />
                  <span className="text-xs text-green-600 bg-green-100 px-2 py-1 rounded-full">+8%</span>
                </div>
                <div className="text-2xl font-bold text-gray-900">{stats.activeDrivers}</div>
                <div className="text-sm text-gray-600">Active Drivers</div>
              </div>
              
              <div className="card p-4">
                <div className="flex items-center justify-between mb-2">
                  <TrendingUp className="w-8 h-8 text-purple-500" />
                  <span className="text-xs text-green-600 bg-green-100 px-2 py-1 rounded-full">+15%</span>
                </div>
                <div className="text-2xl font-bold text-gray-900">{stats.todayTrips.toLocaleString()}</div>
                <div className="text-sm text-gray-600">Today's Trips</div>
              </div>
              
              <div className="card p-4">
                <div className="flex items-center justify-between mb-2">
                  <DollarSign className="w-8 h-8 text-yellow-500" />
                  <span className="text-xs text-green-600 bg-green-100 px-2 py-1 rounded-full">+18%</span>
                </div>
                <div className="text-2xl font-bold text-gray-900">${stats.todayRevenue.toLocaleString()}</div>
                <div className="text-sm text-gray-600">Today's Revenue</div>
              </div>
            </div>

            {/* System Alerts */}
            <div className="card">
              <div className="p-6">
                <div className="flex items-center justify-between mb-4">
                  <h3 className="text-lg font-semibold text-gray-900">System Alerts</h3>
                  <AlertTriangle className="w-5 h-5 text-yellow-500" />
                </div>
                <div className="space-y-3">
                  {systemAlerts.map((alert) => (
                    <div key={alert.id} className={`p-3 rounded-lg border ${
                      alert.type === 'error' ? 'bg-red-50 border-red-200' :
                      alert.type === 'warning' ? 'bg-yellow-50 border-yellow-200' :
                      'bg-blue-50 border-blue-200'
                    }`}>
                      <div className="flex items-start space-x-3">
                        {alert.type === 'error' ? (
                          <XCircle className="w-5 h-5 text-red-500 mt-0.5" />
                        ) : alert.type === 'warning' ? (
                          <AlertTriangle className="w-5 h-5 text-yellow-500 mt-0.5" />
                        ) : (
                          <CheckCircle className="w-5 h-5 text-blue-500 mt-0.5" />
                        )}
                        <div className="flex-1">
                          <div className="text-sm font-medium text-gray-900">{alert.message}</div>
                          <div className="text-xs text-gray-500 mt-1">{alert.time}</div>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>

            {/* Recent Activity */}
            <div className="grid md:grid-cols-2 gap-6">
              <div className="card">
                <div className="p-6">
                  <h3 className="text-lg font-semibold text-gray-900 mb-4">Recent Users</h3>
                  <div className="space-y-3">
                    {recentUsers.slice(0, 3).map((user) => (
                      <div key={user.id} className="flex items-center justify-between">
                        <div className="flex items-center space-x-3">
                          <div className="w-10 h-10 bg-gray-100 rounded-full flex items-center justify-center">
                            <Users className="w-5 h-5 text-gray-600" />
                          </div>
                          <div>
                            <div className="font-medium text-gray-900">{user.name}</div>
                            <div className="text-sm text-gray-600">{user.type}</div>
                          </div>
                        </div>
                        <span className={`px-2 py-1 rounded-full text-xs ${
                          user.status === 'Active' ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-600'
                        }`}>
                          {user.status}
                        </span>
                      </div>
                    ))}
                  </div>
                </div>
              </div>

              <div className="card">
                <div className="p-6">
                  <h3 className="text-lg font-semibold text-gray-900 mb-4">Recent Trips</h3>
                  <div className="space-y-3">
                    {recentTrips.slice(0, 3).map((trip) => (
                      <div key={trip.id} className="flex items-center justify-between">
                        <div>
                          <div className="font-medium text-gray-900">{trip.customer}</div>
                          <div className="text-sm text-gray-600">{trip.pickup} to {trip.destination}</div>
                        </div>
                        <div className="text-right">
                          <div className="font-semibold text-gray-900">{trip.fare}</div>
                          <span className={`px-2 py-1 rounded-full text-xs ${
                            trip.status === 'Completed' ? 'bg-green-100 text-green-800' :
                            trip.status === 'In Progress' ? 'bg-blue-100 text-blue-800' :
                            'bg-red-100 text-red-800'
                          }`}>
                            {trip.status}
                          </span>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            </div>
          </motion.div>
        );

      case 'users':
        return (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="space-y-6"
          >
            <div className="flex items-center justify-between">
              <h3 className="text-xl font-semibold text-gray-900">User Management</h3>
              <button className="btn-primary flex items-center">
                <Plus className="w-4 h-4 mr-2" />
                Add User
              </button>
            </div>

            {/* Search and Filter */}
            <div className="card p-4">
              <div className="flex flex-col md:flex-row gap-4">
                <div className="flex-1 relative">
                  <Search className="absolute left-3 top-3 w-5 h-5 text-gray-400" />
                  <input
                    type="text"
                    placeholder="Search users..."
                    className="input-field pl-10"
                  />
                </div>
                <div className="flex gap-2">
                  <select className="input-field">
                    <option>All Types</option>
                    <option>Customer</option>
                    <option>Driver</option>
                    <option>Admin</option>
                  </select>
                  <select className="input-field">
                    <option>All Status</option>
                    <option>Active</option>
                    <option>Inactive</option>
                    <option>Suspended</option>
                  </select>
                  <button className="btn-secondary flex items-center">
                    <Filter className="w-4 h-4 mr-2" />
                    Filter
                  </button>
                </div>
              </div>
            </div>

            {/* Users Table */}
            <div className="card overflow-hidden">
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead className="bg-gray-50 border-b border-gray-200">
                    <tr>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">User</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Type</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Contact</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Joined</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {recentUsers.map((user) => (
                      <tr key={user.id} className="hover:bg-gray-50">
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="flex items-center">
                            <div className="w-10 h-10 bg-gray-100 rounded-full flex items-center justify-center">
                              <Users className="w-5 h-5 text-gray-600" />
                            </div>
                            <div className="ml-4">
                              <div className="text-sm font-medium text-gray-900">{user.name}</div>
                              <div className="text-sm text-gray-500">ID: {user.id}</div>
                            </div>
                          </div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className={`px-2 py-1 rounded-full text-xs ${
                            user.type === 'Driver' ? 'bg-blue-100 text-blue-800' :
                            user.type === 'Admin' ? 'bg-purple-100 text-purple-800' :
                            'bg-green-100 text-green-800'
                          }`}>
                            {user.type}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm text-gray-900">{user.email}</div>
                          <div className="text-sm text-gray-500">{user.phone}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className={`px-2 py-1 rounded-full text-xs ${
                            user.status === 'Active' ? 'bg-green-100 text-green-800' :
                            user.status === 'Inactive' ? 'bg-gray-100 text-gray-600' :
                            'bg-red-100 text-red-800'
                          }`}>
                            {user.status}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {user.joinDate}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                          <div className="flex space-x-2">
                            <button className="text-blue-600 hover:text-blue-900">
                              <Eye className="w-4 h-4" />
                            </button>
                            <button className="text-green-600 hover:text-green-900">
                              <Edit className="w-4 h-4" />
                            </button>
                            <button className="text-red-600 hover:text-red-900">
                              <Trash2 className="w-4 h-4" />
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </motion.div>
        );

      case 'trips':
        return (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="space-y-6"
          >
            <div className="flex items-center justify-between">
              <h3 className="text-xl font-semibold text-gray-900">Trip Management</h3>
              <div className="flex space-x-2">
                <button className="btn-secondary flex items-center">
                  <Download className="w-4 h-4 mr-2" />
                  Export
                </button>
                <button className="btn-primary flex items-center">
                  <Plus className="w-4 h-4 mr-2" />
                  New Trip
                </button>
              </div>
            </div>

            {/* Trip Stats */}
            <div className="grid grid-cols-4 gap-4">
              <div className="card p-4 text-center">
                <div className="text-2xl font-bold text-gray-900">{stats.totalTrips.toLocaleString()}</div>
                <div className="text-sm text-gray-600">Total Trips</div>
              </div>
              <div className="card p-4 text-center">
                <div className="text-2xl font-bold text-green-600">{stats.todayTrips.toLocaleString()}</div>
                <div className="text-sm text-gray-600">Today</div>
              </div>
              <div className="card p-4 text-center">
                <div className="text-2xl font-bold text-blue-600">${stats.totalRevenue.toLocaleString()}</div>
                <div className="text-sm text-gray-600">Total Revenue</div>
              </div>
              <div className="card p-4 text-center">
                <div className="text-2xl font-bold text-purple-600">{stats.avgRating}</div>
                <div className="text-sm text-gray-600">Avg Rating</div>
              </div>
            </div>

            {/* Trips Table */}
            <div className="card overflow-hidden">
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead className="bg-gray-50 border-b border-gray-200">
                    <tr>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Trip ID</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Customer</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Driver</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Route</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Fare</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {recentTrips.map((trip) => (
                      <tr key={trip.id} className="hover:bg-gray-50">
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                          #{trip.id}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {trip.customer}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {trip.driver}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          <div className="max-w-xs truncate">{trip.pickup} to {trip.destination}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                          {trip.fare}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className={`px-2 py-1 rounded-full text-xs ${
                            trip.status === 'Completed' ? 'bg-green-100 text-green-800' :
                            trip.status === 'In Progress' ? 'bg-blue-100 text-blue-800' :
                            'bg-red-100 text-red-800'
                          }`}>
                            {trip.status}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {trip.date}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                          <div className="flex space-x-2">
                            <button className="text-blue-600 hover:text-blue-900">
                              <Eye className="w-4 h-4" />
                            </button>
                            <button className="text-green-600 hover:text-green-900">
                              <Edit className="w-4 h-4" />
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </motion.div>
        );

      case 'support':
        return (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="space-y-6"
          >
            <div className="flex items-center justify-between">
              <h3 className="text-xl font-semibold text-gray-900">Support Tickets</h3>
              <div className="flex items-center space-x-2">
                <span className="px-3 py-1 bg-red-100 text-red-800 rounded-full text-sm font-medium">
                  {supportTickets.filter(t => t.status === 'Open').length} Open
                </span>
                <button className="btn-primary flex items-center">
                  <Plus className="w-4 h-4 mr-2" />
                  New Ticket
                </button>
              </div>
            </div>

            {/* Support Stats */}
            <div className="grid grid-cols-4 gap-4">
              <div className="card p-4 text-center">
                <div className="text-2xl font-bold text-red-600">{supportTickets.filter(t => t.status === 'Open').length}</div>
                <div className="text-sm text-gray-600">Open</div>
              </div>
              <div className="card p-4 text-center">
                <div className="text-2xl font-bold text-blue-600">{supportTickets.filter(t => t.status === 'In Progress').length}</div>
                <div className="text-sm text-gray-600">In Progress</div>
              </div>
              <div className="card p-4 text-center">
                <div className="text-2xl font-bold text-green-600">{supportTickets.filter(t => t.status === 'Resolved').length}</div>
                <div className="text-sm text-gray-600">Resolved</div>
              </div>
              <div className="card p-4 text-center">
                <div className="text-2xl font-bold text-purple-600">{supportTickets.length}</div>
                <div className="text-sm text-gray-600">Total</div>
              </div>
            </div>

            {/* Tickets Table */}
            <div className="card overflow-hidden">
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead className="bg-gray-50 border-b border-gray-200">
                    <tr>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Ticket ID</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">User</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Subject</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Priority</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {supportTickets.map((ticket) => (
                      <tr key={ticket.id} className="hover:bg-gray-50">
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                          #{ticket.id}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {ticket.user}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {ticket.subject}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className={`px-2 py-1 rounded-full text-xs ${
                            ticket.priority === 'High' ? 'bg-red-100 text-red-800' :
                            ticket.priority === 'Medium' ? 'bg-yellow-100 text-yellow-800' :
                            'bg-green-100 text-green-800'
                          }`}>
                            {ticket.priority}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className={`px-2 py-1 rounded-full text-xs ${
                            ticket.status === 'Open' ? 'bg-red-100 text-red-800' :
                            ticket.status === 'In Progress' ? 'bg-blue-100 text-blue-800' :
                            'bg-green-100 text-green-800'
                          }`}>
                            {ticket.status}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {ticket.date}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                          <div className="flex space-x-2">
                            <button className="text-blue-600 hover:text-blue-900">
                              <Eye className="w-4 h-4" />
                            </button>
                            <button className="text-green-600 hover:text-green-900">
                              <Edit className="w-4 h-4" />
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </motion.div>
        );

      case 'analytics':
        return (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="space-y-6"
          >
            <h3 className="text-xl font-semibold text-gray-900">Analytics Dashboard</h3>

            {/* Revenue Chart Placeholder */}
            <div className="card p-6">
              <h4 className="text-lg font-medium text-gray-900 mb-4">Revenue Overview</h4>
              <div className="h-64 bg-gray-100 rounded-lg flex items-center justify-center">
                <div className="text-center">
                  <BarChart3 className="w-12 h-12 text-gray-400 mx-auto mb-2" />
                  <p className="text-gray-500">Revenue Chart</p>
                  <p className="text-sm text-gray-400">Integration with charting library needed</p>
                </div>
              </div>
            </div>

            {/* User Growth Chart Placeholder */}
            <div className="card p-6">
              <h4 className="text-lg font-medium text-gray-900 mb-4">User Growth</h4>
              <div className="h-64 bg-gray-100 rounded-lg flex items-center justify-center">
                <div className="text-center">
                  <TrendingUp className="w-12 h-12 text-gray-400 mx-auto mb-2" />
                  <p className="text-gray-500">User Growth Chart</p>
                  <p className="text-sm text-gray-400">Integration with charting library needed</p>
                </div>
              </div>
            </div>

            {/* Key Metrics */}
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
              <div className="card p-4">
                <div className="text-sm text-gray-600 mb-1">Avg Trip Value</div>
                <div className="text-2xl font-bold text-gray-900">$18.50</div>
                <div className="text-xs text-green-600">+5% from last month</div>
              </div>
              <div className="card p-4">
                <div className="text-sm text-gray-600 mb-1">Customer Retention</div>
                <div className="text-2xl font-bold text-gray-900">87%</div>
                <div className="text-xs text-green-600">+3% from last month</div>
              </div>
              <div className="card p-4">
                <div className="text-sm text-gray-600 mb-1">Driver Utilization</div>
                <div className="text-2xl font-bold text-gray-900">72%</div>
                <div className="text-xs text-red-600">-2% from last month</div>
              </div>
              <div className="card p-4">
                <div className="text-sm text-gray-600 mb-1">Support Response Time</div>
                <div className="text-2xl font-bold text-gray-900">2.4h</div>
                <div className="text-xs text-green-600">-15% from last month</div>
              </div>
            </div>
          </motion.div>
        );

      case 'settings':
        return (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="space-y-6"
          >
            <h3 className="text-xl font-semibold text-gray-900">System Settings</h3>

            <div className="card">
              <div className="p-6">
                <h4 className="text-lg font-medium text-gray-900 mb-4">Platform Configuration</h4>
                <div className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Platform Commission (%)</label>
                    <input type="number" className="input-field" defaultValue="10" />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Base Fare ($)</label>
                    <input type="number" className="input-field" defaultValue="2.50" />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Per Mile Rate ($)</label>
                    <input type="number" className="input-field" defaultValue="1.25" />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Per Minute Rate ($)</label>
                    <input type="number" className="input-field" defaultValue="0.25" />
                  </div>
                  <button className="btn-primary">Save Settings</button>
                </div>
              </div>
            </div>

            <div className="card">
              <div className="p-6">
                <h4 className="text-lg font-medium text-gray-900 mb-4">System Maintenance</h4>
                <div className="space-y-4">
                  <button className="w-full btn-secondary flex items-center justify-center">
                    <Database className="w-4 h-4 mr-2" />
                    Backup Database
                  </button>
                  <button className="w-full btn-secondary flex items-center justify-center">
                    <FileText className="w-4 h-4 mr-2" />
                    Generate Reports
                  </button>
                  <button className="w-full btn-secondary flex items-center justify-center">
                    <Settings className="w-4 h-4 mr-2" />
                    Clear Cache
                  </button>
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
              <h1 className="text-xl font-bold text-gray-900">Admin Portal</h1>
            </div>
            <div className="flex items-center space-x-3">
              <button className="p-2 hover:bg-gray-100 rounded-lg transition-colors relative">
                <AlertTriangle className="w-5 h-5 text-gray-600" />
                <span className="absolute top-1 right-1 w-2 h-2 bg-red-500 rounded-full"></span>
              </button>
              <button className="p-2 hover:bg-gray-100 rounded-lg transition-colors">
                <Settings className="w-5 h-5 text-gray-600" />
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Admin Navigation */}
      <nav className="bg-white border-b border-gray-200">
        <div className="px-4">
          <div className="flex space-x-6 overflow-x-auto">
            <button
              onClick={() => setActiveTab('dashboard')}
              className={`py-3 px-1 border-b-2 font-medium text-sm transition-colors whitespace-nowrap ${
                activeTab === 'dashboard' 
                  ? 'border-primary-500 text-primary-600' 
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              Dashboard
            </button>
            <button
              onClick={() => setActiveTab('users')}
              className={`py-3 px-1 border-b-2 font-medium text-sm transition-colors whitespace-nowrap ${
                activeTab === 'users' 
                  ? 'border-primary-500 text-primary-600' 
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              Users
            </button>
            <button
              onClick={() => setActiveTab('trips')}
              className={`py-3 px-1 border-b-2 font-medium text-sm transition-colors whitespace-nowrap ${
                activeTab === 'trips' 
                  ? 'border-primary-500 text-primary-600' 
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              Trips
            </button>
            <button
              onClick={() => setActiveTab('support')}
              className={`py-3 px-1 border-b-2 font-medium text-sm transition-colors whitespace-nowrap ${
                activeTab === 'support' 
                  ? 'border-primary-500 text-primary-600' 
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              Support
            </button>
            <button
              onClick={() => setActiveTab('analytics')}
              className={`py-3 px-1 border-b-2 font-medium text-sm transition-colors whitespace-nowrap ${
                activeTab === 'analytics' 
                  ? 'border-primary-500 text-primary-600' 
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              Analytics
            </button>
            <button
              onClick={() => setActiveTab('settings')}
              className={`py-3 px-1 border-b-2 font-medium text-sm transition-colors whitespace-nowrap ${
                activeTab === 'settings' 
                  ? 'border-primary-500 text-primary-600' 
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              Settings
            </button>
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <main className="p-6">
        {renderContent()}
      </main>
    </div>
  );
};

export default AdminInterface;
