# Ride Hailing Platform - Frontend

A modern, responsive frontend application for the Ride Hailing Platform built with React 18, TailwindCSS, and Framer Motion.

## Features

### Customer Interface
- **Ride Booking**: Easy-to-use booking interface with real-time fare estimation
- **Service Selection**: Multiple vehicle categories (Standard, Comfort, Premium, Bike)
- **Payment Options**: Support for wallet, UPI, and cash payments
- **Trip Tracking**: Real-time tracking of active rides
- **Ride History**: Complete trip history with ratings and receipts
- **Profile Management**: User profile and preferences

### Driver Interface
- **Online/Offline Status**: Toggle availability to receive trip requests
- **Trip Management**: Accept, reject, and complete trips
- **Earnings Dashboard**: Detailed earnings overview and analytics
- **Trip History**: Complete trip history with customer ratings
- **Profile Management**: Driver profile and vehicle information

### Administrator Interface
- **Dashboard Overview**: System statistics and key metrics
- **User Management**: Manage customers, drivers, and administrators
- **Trip Management**: Monitor and manage all trips
- **Support System**: Handle customer support tickets
- **Analytics**: Revenue reports and user growth analytics
- **System Settings**: Platform configuration and maintenance

### Landing Page
- **User Type Selection**: Beautiful, animated interface for role selection
- **Feature Showcase**: Platform features and benefits
- **Responsive Design**: Optimized for all devices

## Technology Stack

- **React 18**: Modern React with hooks and concurrent features
- **React Router 6**: Client-side routing
- **TailwindCSS**: Utility-first CSS framework
- **Framer Motion**: Smooth animations and transitions
- **Lucide React**: Beautiful icon library
- **React Hot Toast**: Elegant notification system
- **Axios**: HTTP client for API communication

## Getting Started

### Prerequisites

- Node.js 16 or higher
- npm or yarn

### Installation

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

The application will be available at `http://localhost:3000`

### Building for Production

```bash
npm run build
```

## Project Structure

```
frontend/
src/
  components/
    LandingPage.js          # Main landing page
    customer/
      CustomerInterface.js  # Customer portal
    driver/
      DriverInterface.js    # Driver portal
    admin/
      AdminInterface.js     # Admin portal
  services/
    api.js                  # API service layer
  App.js                    # Main application component
  index.js                  # Application entry point
  index.css                 # Global styles
```

## API Integration

The frontend is configured to communicate with the backend API running on `http://localhost:8080`. The API service layer (`src/services/api.js`) provides:

- **Authentication**: User registration and login
- **Booking**: Trip booking and management
- **Payment**: Payment processing and history
- **Admin**: System administration endpoints
- **Driver**: Driver-specific operations
- **Customer**: Customer-specific operations

## Key Features

### Responsive Design
- Mobile-first approach
- Adaptive layouts for tablets and desktops
- Touch-friendly interface elements

### Animations
- Smooth page transitions
- Interactive hover states
- Loading animations
- Micro-interactions

### User Experience
- Intuitive navigation
- Clear visual hierarchy
- Consistent design language
- Accessibility considerations

### Error Handling
- Graceful error states
- User-friendly error messages
- Network error recovery
- Form validation

## Configuration

### Backend URL
Update the base URL in `src/services/api.js` to match your backend server:

```javascript
const api = axios.create({
  baseURL: 'http://localhost:8080/api', // Change this if needed
  // ...
});
```

### Environment Variables
Create a `.env` file in the root directory for environment-specific configuration:

```
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_MAP_API_KEY=your_map_api_key
```

## Development

### Code Style
- ESLint configuration for code quality
- Prettier for consistent formatting
- Component-based architecture
- Custom hooks for reusable logic

### Best Practices
- Component composition over inheritance
- Prop validation with PropTypes
- Semantic HTML elements
- Performance optimization with React.memo

## Browser Support

- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

## Contributing

1. Follow the existing code style
2. Write meaningful commit messages
3. Test your changes thoroughly
4. Update documentation as needed

## Deployment

### Netlify
```bash
npm run build
# Deploy the build/ directory to Netlify
```

### Vercel
```bash
npm run build
# Deploy the build/ directory to Vercel
```

### Docker
```dockerfile
FROM node:16-alpine
WORKDIR /app
COPY package*.json ./
RUN npm ci --only=production
COPY . .
RUN npm run build
EXPOSE 3000
CMD ["npm", "start"]
```

## Troubleshooting

### Common Issues

1. **CORS Errors**: Ensure the backend allows requests from your frontend domain
2. **Network Errors**: Check if the backend server is running
3. **Build Errors**: Verify all dependencies are installed correctly

### Performance Tips

- Use React.memo for expensive components
- Implement virtual scrolling for large lists
- Optimize images and assets
- Enable code splitting for larger applications

## License

This project is licensed under the MIT License.
