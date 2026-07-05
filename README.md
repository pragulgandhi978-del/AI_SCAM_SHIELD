AI Scam Shield - Full Stack Scam Detection Application
========================================================

## Quick Start (Windows)

1. **Run the batch script to start both backend and frontend:**
   ```
   double-click: start_all.bat
   ```

Or manually:

### Backend
```
cd backend
mvnw.cmd spring-boot:run
```
Backend runs on: http://localhost:8080

### Frontend
```
cd frontend
npm install
npm run dev
```
Frontend runs on: http://localhost:5173

---

## Project Structure

- **backend/** - Spring Boot REST API
- **frontend/** - React + Vite web application

---

## Key Features

✅ User Registration & Authentication (JWT)
✅ AI-powered Scam Detection (OpenAI + Google Safe Browsing)
✅ Message & URL Scanning
✅ Scan History with Pagination
✅ Scam Reporting System
✅ Admin Dashboard
✅ Rate Limiting (10 scans/hour per user)
✅ CORS-enabled REST API
✅ H2 Database (auto-setup) + PostgreSQL support

---

## Default Credentials

**User:** Sign up from the registration page

**Admin:**
- Username: `admin`
- Password: `admin1234`
- ⚠️ Change in production!

---

## API Documentation

See [SETUP_AND_DEPLOYMENT.md](./SETUP_AND_DEPLOYMENT.md) for:
- Complete API endpoints
- Configuration guide
- Production deployment steps
- Troubleshooting
- Security checklist

---

## Technology Stack

**Backend:**
- Spring Boot 3.3.4
- Spring Security + JWT
- Spring Data JPA
- H2 / PostgreSQL

**Frontend:**
- React 18.3.1
- Vite
- React Router v6
- Axios
- Tailwind CSS

---

## External APIs

The application integrates with:
- **OpenAI GPT-4o-mini** - For message scam detection
- **Google Safe Browsing API v4** - For URL threat checking

Set API keys as environment variables:
```
OPENAI_API_KEY=your-key
SAFE_BROWSING_API_KEY=your-key
```

---

## Environment Setup

### Backend Configuration
File: `backend/src/main/resources/application.properties`
- Database: H2 (in-memory) or PostgreSQL
- Port: 8080
- JWT Secret: `scamshield-dev-secret-key-change-in-production-xyz`

### Frontend Configuration
File: `frontend/.env.local`
```
VITE_API_URL=http://localhost:8080/api
```

---

## Support

For detailed setup, deployment, and troubleshooting: See [SETUP_AND_DEPLOYMENT.md](./SETUP_AND_DEPLOYMENT.md)

---

Created: July 2026
