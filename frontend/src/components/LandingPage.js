import React from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { Car, User, Shield, ArrowRight, MapPin, Clock, Star, UserPlus } from 'lucide-react';

const LandingPage = ({ onLoginRedirect, onRegisterRedirect }) => {
  const navigate = useNavigate();

  const userTypes = [
    {
      id: 'customer',
      title: 'Customer',
      description: 'Book rides, track trips, and manage your travel needs',
      icon: User,
      color: 'from-blue-500 to-blue-600',
      hoverColor: 'hover:from-blue-600 hover:to-blue-700',
      features: ['Easy Booking', 'Real-time Tracking', 'Secure Payments', '24/7 Support']
    },
    {
      id: 'driver',
      title: 'Driver',
      description: 'Accept rides, manage earnings, and grow your business',
      icon: Car,
      color: 'from-green-500 to-green-600',
      hoverColor: 'hover:from-green-600 hover:to-green-700',
      features: ['Flexible Schedule', 'Competitive Earnings', 'Trip Management', 'Performance Analytics']
    },
    {
      id: 'admin',
      title: 'Administrator',
      description: 'Manage system operations, monitor performance, and ensure quality',
      icon: Shield,
      color: 'from-purple-500 to-purple-600',
      hoverColor: 'hover:from-purple-600 hover:to-purple-700',
      features: ['System Monitoring', 'User Management', 'Analytics Dashboard', 'Support Tickets']
    }
  ];

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.1
      }
    }
  };

  const itemVariants = {
    hidden: { y: 20, opacity: 0 },
    visible: {
      y: 0,
      opacity: 1,
      transition: {
        type: "spring",
        stiffness: 100
      }
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-indigo-50 to-purple-50">
      {/* Header */}
      <header className="bg-white/80 backdrop-blur-md border-b border-gray-100 sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center space-x-3">
              <div className="w-10 h-10 bg-gradient-to-r from-primary-500 to-primary-600 rounded-lg flex items-center justify-center">
                <Car className="w-6 h-6 text-white" />
              </div>
              <h1 className="text-2xl font-bold text-gray-900">RideHail</h1>
            </div>
            <nav className="hidden md:flex space-x-8">
              <a href="#features" className="text-gray-600 hover:text-primary-600 transition-colors">Features</a>
              <a href="#about" className="text-gray-600 hover:text-primary-600 transition-colors">About</a>
              <a href="#contact" className="text-gray-600 hover:text-primary-600 transition-colors">Contact</a>
            </nav>
          </div>
        </div>
      </header>

      {/* Hero Section */}
      <section className="py-20 px-4 sm:px-6 lg:px-8">
        <div className="max-w-7xl mx-auto text-center">
          <motion.h1 
            className="text-5xl md:text-6xl font-bold text-gray-900 mb-6"
            initial={{ opacity: 0, y: -20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6 }}
          >
            Welcome to
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-primary-600 to-secondary-600 ml-3">
              RideHail
            </span>
          </motion.h1>
          <motion.p 
            className="text-xl text-gray-600 mb-8 max-w-3xl mx-auto"
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: 0.2 }}
          >
            Your comprehensive ride-hailing platform. Choose your role below to get started with our smart, efficient, and user-friendly transportation solution.
          </motion.p>
        </div>
      </section>

      {/* User Type Selection */}
      <section className="py-16 px-4 sm:px-6 lg:px-8">
        <div className="max-w-7xl mx-auto">
          <motion.h2 
            className="text-3xl font-bold text-center text-gray-900 mb-12"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ duration: 0.6, delay: 0.4 }}
          >
            Choose Your Role
          </motion.h2>
          
          <motion.div 
            className="grid md:grid-cols-3 gap-8"
            variants={containerVariants}
            initial="hidden"
            animate="visible"
          >
            {userTypes.map((userType) => {
              const Icon = userType.icon;
              return (
                <motion.div
                  key={userType.id}
                  variants={itemVariants}
                  whileHover={{ scale: 1.05 }}
                  whileTap={{ scale: 0.95 }}
                >
                  <div 
                    className={`card cursor-pointer transform transition-all duration-300 hover:shadow-2xl group`}
                  >
                    <div className={`p-8 text-center`}>
                      <div className={`w-20 h-20 mx-auto mb-6 bg-gradient-to-r ${userType.color} rounded-full flex items-center justify-center transform transition-transform duration-300 group-hover:scale-110`}>
                        <Icon className="w-10 h-10 text-white" />
                      </div>
                      <h3 className="text-2xl font-bold text-gray-900 mb-3">{userType.title}</h3>
                      <p className="text-gray-600 mb-6">{userType.description}</p>
                      
                      <ul className="space-y-3 mb-8 text-left">
                        {userType.features.map((feature, index) => (
                          <li key={index} className="flex items-center text-gray-700">
                            <div className="w-2 h-2 bg-primary-500 rounded-full mr-3"></div>
                            {feature}
                          </li>
                        ))}
                      </ul>
                      
                      <div className="space-y-3">
                        <button 
                          onClick={() => onLoginRedirect(userType.id.toUpperCase())}
                          className={`w-full bg-gradient-to-r ${userType.color} ${userType.hoverColor} text-white font-semibold py-3 px-6 rounded-lg transition-all duration-300 flex items-center justify-center group-hover:shadow-lg`}
                        >
                          Login as {userType.title}
                          <ArrowRight className="w-5 h-5 ml-2 transform transition-transform duration-300 group-hover:translate-x-1" />
                        </button>
                        <button 
                          onClick={() => onRegisterRedirect?.(userType.id.toUpperCase())}
                          className={`w-full bg-gray-100 hover:bg-gray-200 text-gray-800 font-semibold py-3 px-6 rounded-lg transition-all duration-300 flex items-center justify-center`}
                        >
                          <UserPlus className="w-5 h-5 mr-2" />
                          Register as {userType.title}
                        </button>
                      </div>
                    </div>
                  </div>
                </motion.div>
              );
            })}
          </motion.div>
        </div>
      </section>

      {/* Features Section */}
      <section id="features" className="py-20 px-4 sm:px-6 lg:px-8 bg-white/50">
        <div className="max-w-7xl mx-auto">
          <h2 className="text-3xl font-bold text-center text-gray-900 mb-12">Platform Features</h2>
          <div className="grid md:grid-cols-3 gap-8">
            <div className="text-center">
              <div className="w-16 h-16 bg-primary-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <MapPin className="w-8 h-8 text-primary-600" />
              </div>
              <h3 className="text-xl font-semibold text-gray-900 mb-2">Real-time Tracking</h3>
              <p className="text-gray-600">Track your rides in real-time with accurate GPS positioning</p>
            </div>
            <div className="text-center">
              <div className="w-16 h-16 bg-primary-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <Clock className="w-8 h-8 text-primary-600" />
              </div>
              <h3 className="text-xl font-semibold text-gray-900 mb-2">Quick Matching</h3>
              <p className="text-gray-600">Smart algorithm matches you with the best available driver instantly</p>
            </div>
            <div className="text-center">
              <div className="w-16 h-16 bg-primary-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <Star className="w-8 h-8 text-primary-600" />
              </div>
              <h3 className="text-xl font-semibold text-gray-900 mb-2">Rating System</h3>
              <p className="text-gray-600">Rate your experience and help improve service quality</p>
            </div>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-gray-900 text-white py-12 px-4 sm:px-6 lg:px-8">
        <div className="max-w-7xl mx-auto text-center">
          <div className="flex items-center justify-center space-x-3 mb-4">
            <div className="w-8 h-8 bg-gradient-to-r from-primary-500 to-primary-600 rounded-lg flex items-center justify-center">
              <Car className="w-5 h-5 text-white" />
            </div>
            <h3 className="text-xl font-bold">RideHail</h3>
          </div>
          <p className="text-gray-400">© 2024 RideHail Platform. All rights reserved.</p>
        </div>
      </footer>
    </div>
  );
};

export default LandingPage;
