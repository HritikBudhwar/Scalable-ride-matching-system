import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import LandingPage from './components/LandingPage';
import Login from './components/Login';
import CustomerRegistration from './components/CustomerRegistration';
import DriverRegistration from './components/DriverRegistration';
import AdminRegistration from './components/AdminRegistration';
import CustomerInterface from './components/customer/CustomerInterface';
import DriverInterface from './components/driver/DriverInterface';
import AdminInterface from './components/admin/AdminInterface';
import './index.css';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [userInfo, setUserInfo] = useState(null);
  const [loginType, setLoginType] = useState(null);
  const [mode, setMode] = useState('landing'); // landing, login, registration

  useEffect(() => {
    const token = localStorage.getItem('authToken');
    const storedUserInfo = localStorage.getItem('userInfo');
    
    if (token && storedUserInfo) {
      try {
        const parsedUserInfo = JSON.parse(storedUserInfo);
        setIsAuthenticated(true);
        setUserInfo(parsedUserInfo);
      } catch (error) {
        console.error('Failed to parse user info:', error);
        localStorage.removeItem('authToken');
        localStorage.removeItem('userInfo');
      }
    }
  }, []);

  const handleLogin = (userData) => {
    setIsAuthenticated(true);
    setUserInfo(userData);
    setMode('landing');
    setLoginType(null);
  };

  const handleLogout = () => {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userInfo');
    setIsAuthenticated(false);
    setUserInfo(null);
    setLoginType(null);
    setMode('landing');
  };

  const handleLoginRedirect = (userType) => {
    setLoginType(userType);
    setMode('login');
  };

  const handleRegistrationRedirect = (userType) => {
    setLoginType(userType);
    setMode('registration');
  };

  const handleBackClick = () => {
    setMode('landing');
    setLoginType(null);
  };

  if (isAuthenticated && userInfo) {
    return (
      <Router>
        <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
          <Routes>
            <Route path="/" element={<Navigate to={`/${userInfo.userType.toLowerCase()}`} replace />} />
            <Route path="/customer" element={<CustomerInterface userInfo={userInfo} onLogout={handleLogout} />} />
            <Route path="/driver" element={<DriverInterface userInfo={userInfo} onLogout={handleLogout} />} />
            <Route path="/admin" element={<AdminInterface userInfo={userInfo} onLogout={handleLogout} />} />
            <Route path="*" element={<Navigate to={`/${userInfo.userType.toLowerCase()}`} replace />} />
          </Routes>
          <Toaster
            position="top-right"
            toastOptions={{
              duration: 4000,
              style: {
                background: '#363636',
                color: '#fff',
              },
              success: {
                duration: 3000,
                iconTheme: {
                  primary: '#22c55e',
                  secondary: '#fff',
                },
              },
              error: {
                duration: 5000,
                iconTheme: {
                  primary: '#ef4444',
                  secondary: '#fff',
                },
              },
            }}
          />
        </div>
      </Router>
    );
  }

  if (loginType && mode === 'login') {
    return (
      <Router>
        <Login 
          onLogin={handleLogin} 
          userType={loginType}
          onGoToRegistration={() => setMode('registration')}
          onBack={handleBackClick}
        />
        <Toaster
          position="top-right"
          toastOptions={{
            duration: 4000,
            style: {
              background: '#363636',
              color: '#fff',
            },
            success: {
              duration: 3000,
              iconTheme: {
                primary: '#22c55e',
                secondary: '#fff',
              },
            },
            error: {
              duration: 5000,
              iconTheme: {
                primary: '#ef4444',
                secondary: '#fff',
              },
            },
          }}
        />
      </Router>
    );
  }

  if (loginType && mode === 'registration') {
    const RegistrationComponent = 
      loginType === 'CUSTOMER' ? CustomerRegistration :
      loginType === 'DRIVER' ? DriverRegistration :
      loginType === 'ADMIN' ? AdminRegistration : null;

    return (
      <Router>
        {RegistrationComponent ? (
          <RegistrationComponent 
            onRegistrationSuccess={handleLogin}
            onBackClick={handleBackClick}
          />
        ) : null}
        <Toaster
          position="top-right"
          toastOptions={{
            duration: 4000,
            style: {
              background: '#363636',
              color: '#fff',
            },
            success: {
              duration: 3000,
              iconTheme: {
                primary: '#22c55e',
                secondary: '#fff',
              },
            },
            error: {
              duration: 5000,
              iconTheme: {
                primary: '#ef4444',
                secondary: '#fff',
              },
            },
          }}
        />
      </Router>
    );
  }

  if (loginType) {
    return (
      <Router>
        <Login onLogin={handleLogin} userType={loginType} />
        <Toaster
          position="top-right"
          toastOptions={{
            duration: 4000,
            style: {
              background: '#363636',
              color: '#fff',
            },
            success: {
              duration: 3000,
              iconTheme: {
                primary: '#22c55e',
                secondary: '#fff',
              },
            },
            error: {
              duration: 5000,
              iconTheme: {
                primary: '#ef4444',
                secondary: '#fff',
              },
            },
          }}
        />
      </Router>
    );
  }

  return (
    <Router>
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
        <Routes>
          <Route 
            path="/" 
            element={
              <LandingPage 
                onLoginRedirect={handleLoginRedirect}
                onRegisterRedirect={handleRegistrationRedirect}
              />
            } 
          />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
        <Toaster
          position="top-right"
          toastOptions={{
            duration: 4000,
            style: {
              background: '#363636',
              color: '#fff',
            },
            success: {
              duration: 3000,
              iconTheme: {
                primary: '#22c55e',
                secondary: '#fff',
              },
            },
            error: {
              duration: 5000,
              iconTheme: {
                primary: '#ef4444',
                secondary: '#fff',
              },
            },
          }}
        />
      </div>
    </Router>
  );
}

export default App;
