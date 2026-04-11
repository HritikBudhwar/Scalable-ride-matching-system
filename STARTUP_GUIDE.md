# 🚀 Complete Startup Guide - Ride Hailing Platform

## Prerequisites
- Java 11+ installed
- Node.js 14+ and npm installed
- Maven installed

---

## ✅ Step 1: Start the Backend (Spring Boot)

### Terminal 1 - Backend
```bash
cd /home/hritik-budhwar/Downloads/Scalable-ride-matching-system-feature-guruprasad
mvn clean install -DskipTests
mvn spring-boot:run
```

**Expected Output:**
```
Tomcat started on port(s): 8080
Started Application in XX seconds
```

**Verify Backend is Running:**
- Open browser: http://localhost:8080/api/auth/validate
- Should see error (expected, no token)

---

## ✅ Step 2: Start the Frontend (React)

### Terminal 2 - Frontend
```bash
cd /home/hritik-budhwar/Downloads/Scalable-ride-matching-system-feature-guruprasad/frontend
npm install
npm start
```

**Expected Output:**
```
Compiled successfully!
On Your Network: http://192.168.x.x:3000
```

**Verify Frontend is Running:**
- Browser should auto-open: http://localhost:3000
- You should see the Landing Page with three buttons: Customer, Driver, Administrator

---

## 🔐 Login Credentials (Demo Mode - ANY Email/Password Works)

### Customer Login
1. Click **"Customer"** button on landing page
2. Enter credentials:
   - **Email**: `customer@example.com`
   - **Password**: `password123`
3. Click **"Sign In"**

### Driver Login
1. Click **"Driver"** button on landing page
2. Enter credentials:
   - **Email**: `driver@example.com`
   - **Password**: `password123`
3. Click **"Sign In"**

### Admin Login
1. Click **"Administrator"** button on landing page
2. Enter credentials:
   - **Email**: `admin@example.com`
   - **Password**: `password123`
3. Click **"Sign In"**

---

## 🔗 Verify Frontend-Backend Connection

### Check Network Requests:
1. Open browser DevTools: `F12` or `Right Click → Inspect`
2. Go to **Network** tab
3. Try logging in
4. You should see:
   - ✅ `POST /api/auth/login` - Status 200
   - Response: `{"token": "...", "userId": 1, "userType": "CUSTOMER", ...}`

### Check Browser Console:
1. Open DevTools → **Console** tab
2. Look for messages like:
   - ✅ "Login successful!" (green toast notification)
   - Log entry: `Login successful` with response data
   - No red error messages

---

## 📊 Database Access

### H2 Console (In-Memory Database)
- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:ride_hailing;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`
- **Username**: `sa`
- **Password**: (leave empty)

---

## 🔧 API Endpoints (Working)

### Authentication
- `POST /api/auth/login` - Login with email, password, userType
- `POST /api/auth/logout` - Logout with token
- `GET /api/auth/validate` - Validate token

### Example Request:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "customer@example.com",
    "password": "password123",
    "userType": "CUSTOMER"
  }'
```

**Expected Response:**
```json
{
  "token": "token_1_1649521234000",
  "userId": 1,
  "userType": "CUSTOMER",
  "email": "customer@example.com",
  "firstName": "CUSTOMER",
  "lastName": "User",
  "message": "Login successful"
}
```

---

## 🐛 Troubleshooting

### **Problem: "Login failed. Please try again."**
1. Check backend is running: http://localhost:8080/api/auth/validate
2. Check browser console (F12) for network errors
3. Verify CORS headers in Network tab:
   - Should see: `Access-Control-Allow-Origin: http://localhost:3000`
4. Restart both backend and frontend

### **Problem: "Cannot GET /api/..."**
1. Backend not running on port 8080
2. Start backend: `mvn spring-boot:run` in project root directory
3. Verify: http://localhost:8080/api/auth/validate

### **Problem: Blank page or "Failed to compile" in frontend**
1. Check if npm dependencies installed: `npm install`
2. Clear node_modules: `rm -rf node_modules && npm install`
3. Restart: `npm start`

### **Problem: Port already in use**
- Backend port 8080:
  ```bash
  lsof -i :8080  # Find process
  kill -9 <PID>  # Kill process
  ```
- Frontend port 3000:
  ```bash
  lsof -i :3000
  kill -9 <PID>
  ```

---

## 📝 Files Modified/Created

### Backend (Java)
- ✅ `src/main/java/com/platform/config/CorsConfig.java` - Global CORS configuration
- ✅ `src/main/java/com/platform/config/CustomErrorController.java` - Error handling
- ✅ `src/main/java/com/platform/controller/AuthController.java` - Fixed login endpoint
- ✅ `src/main/java/com/platform/dto/request/LoginRequestDTO.java` - Login request DTO
- ✅ `src/main/java/com/platform/dto/response/LoginResponseDTO.java` - Login response DTO
- ✅ `src/main/resources/application.yml` - Updated server configuration

### Frontend (React)
- ✅ `frontend/src/services/api.js` - Already configured correctly
- ✅ `frontend/src/components/Login.js` - Already configured correctly

---

## ✨ What's Fixed

1. ✅ **CORS Configuration**: Global CORS setup allows frontend to communicate with backend
2. ✅ **Login Endpoint**: Now accepts email/password (not phone/OTP)
3. ✅ **DTOs**: Created proper request/response DTOs
4. ✅ **Error Handling**: Proper error responses instead of whitelabel pages
5. ✅ **Authentication**: Backend generates tokens properly
6. ✅ **Frontend-Backend Communication**: API endpoints properly configured

---

## 🎯 Next Steps After Login

### Customer Interface:
- View available rides
- Book rides
- View ride history
- Manage payment methods
- Rate drivers

### Driver Interface:
- Go online/offline
- Accept/reject ride requests
- View earnings
- Complete trips
- Accept ratings

### Admin Interface:
- View system statistics
- Manage users (approve/reject drivers)
- Monitor trips
- View system settings

---

## 💡 Notes

- **Demo Mode**: System accepts ANY email and password for demo purposes
- **In-Memory Database**: Data resets when backend restarts
- **Token**: Valid for the current session only
- **CORS**: Only allows requests from `http://localhost:3000`

---

## 📞 Support

If you encounter any issues:
1. Check this guide's **Troubleshooting** section
2. Verify both backend and frontend are running
3. Check network requests in DevTools (F12 → Network)
4. Check console for error messages (F12 → Console)

---

**Happy Testing! 🎉**
