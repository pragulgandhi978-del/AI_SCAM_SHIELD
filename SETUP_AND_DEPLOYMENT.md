# AI Scam Shield - Complete Setup & Deployment Guide

## Project Overview

AI Scam Shield is a full-stack web application for detecting and reporting scams using AI analysis. It features:
- **User Authentication**: JWT-based authentication for both regular users and admins
- **Scam Detection**: AI-powered message and URL analysis using OpenAI and Google Safe Browsing
- **Scan History**: Persistent storage of all scans with risk assessments
- **Admin Dashboard**: Overview of all users and reported scams
- **Rate Limiting**: Per-user rate limiting (10 scans per hour)

---

## Architecture

### Backend
- **Framework**: Spring Boot 3.3.4
- **Database**: H2 (in-memory, auto-setup) or PostgreSQL (production)
- **Authentication**: JWT tokens with Spring Security
- **APIs**: RESTful endpoints with validation
- **External Services**: OpenAI (GPT-4o-mini), Google Safe Browsing

### Frontend
- **Framework**: React 18.3.1 with Vite
- **Routing**: React Router v6
- **HTTP Client**: Axios with JWT interceptor
- **Styling**: Tailwind CSS
- **State Management**: React Context API

---

## Prerequisites

### Required
- **Java 21+** (for backend)
- **Node.js 18+** (for frontend development)
- **Maven 3.8+** (for backend build)
- **Git** (for version control)

### Optional (Development)
- **Docker** (for containerized deployment)
- **PostgreSQL 14+** (for production database)

---

## Quick Start (Development)

### 1. Backend Setup & Run

#### Windows
```bash
cd d:\project AI SS\backend

# Build the project
mvnw.cmd clean package

# Run the application
mvnw.cmd spring-boot:run
```

#### Linux/macOS
```bash
cd "d:\project AI SS\backend"

# Build the project
./mvnw clean package

# Run the application
./mvnw spring-boot:run
```

The backend will start on **http://localhost:8080**

#### Default Credentials
- **Admin Login**: 
  - Username: `admin`
  - Password: `admin1234`
  - ⚠️ **Change in production!**

### 2. Frontend Setup & Run

```bash
cd "d:\project AI SS\frontend"

# Install dependencies
npm install

# Start development server
npm run dev
```

The frontend will start on **http://localhost:5173**

### 3. Access the Application

Open your browser and navigate to:
- **User App**: http://localhost:5173
- **API Health Check**: http://localhost:8080/api/health
- **H2 Database Console**: http://localhost:8080/h2-console (dev only)
  - JDBC URL: `jdbc:h2:mem:scamshielddb`
  - Username: `sa`
  - Password: (leave empty)

---

## Configuration

### Backend Configuration

#### File: `backend/src/main/resources/application.properties`

**Database Setup** (currently H2 in-memory):
```properties
# H2 (Development - default)
spring.datasource.url=jdbc:h2:mem:scamshielddb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# PostgreSQL (Uncomment for production)
# spring.datasource.url=jdbc:postgresql://localhost:5432/scamshield
# spring.datasource.driver-class-name=org.postgresql.Driver
# spring.datasource.username=scamshield
# spring.datasource.password=${DB_PASSWORD}
# spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

**API Keys** (Environment Variables):
```bash
# Set these before running in production
export JWT_SECRET="your-very-strong-secret-key-min-32-chars"
export OPENAI_API_KEY="sk-your-openai-key"
export SAFE_BROWSING_API_KEY="your-google-api-key"
export JWT_EXPIRATION_MS=86400000  # 24 hours
```

**External API Timeouts**:
```properties
external.api.connect-timeout-ms=10000
external.api.read-timeout-ms=10000
```

### Frontend Configuration

#### File: `frontend/.env.local`
```
VITE_API_URL=http://localhost:8080/api
```

For production:
```
VITE_API_URL=https://api.yourdomain.com/api
```

---

## API Endpoints

### Authentication
- `POST /api/register` - User registration
- `POST /api/login` - User login
- `POST /api/admin/login` - Admin login

### Scanning (Protected)
- `POST /api/scan-message` - Analyze a message for scam risk
- `POST /api/scan-url` - Check URL against known malicious sites

### User Features (Protected)
- `GET /api/dashboard` - Get scan statistics
- `GET /api/history?page=0&size=10` - Get scan history with pagination
- `POST /api/report-scam` - Report a scam

### Admin Features (Protected, ADMIN role required)
- `GET /api/admin/users` - List all users
- `GET /api/admin/reports` - List all scam reports

### Public
- `GET /api/health` - Health check

---

## Build & Production Deployment

### Backend Build

```bash
cd backend
mvnw clean package -DskipTests
# Output: target/backend-0.0.1-SNAPSHOT.jar
```

### Run as JAR

```bash
java -jar backend-0.0.1-SNAPSHOT.jar
```

### Docker Deployment (Optional)

**Create Dockerfile for Backend:**
```dockerfile
FROM eclipse-temurin:21-jre-alpine
COPY backend/target/backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

**Build and Run:**
```bash
docker build -t ai-scam-shield-backend .
docker run -p 8080:8080 \
  -e JWT_SECRET="your-secret" \
  -e OPENAI_API_KEY="your-key" \
  -e SAFE_BROWSING_API_KEY="your-key" \
  ai-scam-shield-backend
```

### Frontend Build

```bash
cd frontend
npm run build
# Output: dist/ directory
```

**Deploy to Static Host** (Vercel, Netlify, GitHub Pages, AWS S3, etc.):
```bash
npm run build
# Upload dist/ folder to your hosting provider
```

**Docker Deployment (Optional):**
```dockerfile
FROM node:18-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

---

## Database Migration

### From H2 to PostgreSQL

1. **Create PostgreSQL Database:**
```sql
CREATE DATABASE scamshield;
CREATE USER scamshield WITH PASSWORD 'secure_password';
GRANT ALL PRIVILEGES ON DATABASE scamshield TO scamshield;
```

2. **Update `application.properties`:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/scamshield
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=scamshield
spring.datasource.password=${DB_PASSWORD}
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=validate
```

3. **Run Migration:**
```bash
# The schema.sql will execute automatically
mvn spring-boot:run
```

---

## Development Workflow

### Make Changes

1. **Frontend**:
   ```bash
   cd frontend
   npm run dev  # Auto-reloads on file changes
   ```

2. **Backend**:
   ```bash
   cd backend
   mvnw spring-boot:run  # Auto-reloads on annotation changes (with DevTools)
   ```

### Running Tests

**Backend:**
```bash
cd backend
mvnw test
```

**Frontend:**
```bash
cd frontend
npm test
```

### Building for Production

**Backend:**
```bash
cd backend
mvnw clean package -DskipTests
```

**Frontend:**
```bash
cd frontend
npm run build
```

---

## Troubleshooting

### Backend Issues

**Port 8080 Already in Use:**
```bash
# Find process using port 8080
netstat -ano | findstr :8080  # Windows
lsof -i :8080  # Linux/macOS

# Kill the process and restart
```

**H2 Console Not Accessible:**
- Verify H2 is enabled: `spring.h2.console.enabled=true`
- Access via: http://localhost:8080/h2-console

**JWT Token Errors:**
- Check `JWT_SECRET` is set and at least 32 characters
- Verify token format: `Bearer <token>`

### Frontend Issues

**API Connection Refused:**
- Ensure backend is running on localhost:8080
- Check `.env.local` has correct `VITE_API_URL`
- Clear browser cache and reload

**CORS Errors:**
- Verify SecurityConfig has CORS bean configured
- Check allowed origins includes your frontend URL

**Build Errors:**
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
npm run build
```

---

## Security Checklist

### Production Deployment
- [ ] Set strong `JWT_SECRET` (min 32 random characters)
- [ ] Set real `OPENAI_API_KEY` and `SAFE_BROWSING_API_KEY`
- [ ] Change default admin password
- [ ] Switch from H2 to PostgreSQL
- [ ] Enable HTTPS/SSL
- [ ] Configure CORS for your domain only
- [ ] Set up rate limiting on API endpoints
- [ ] Enable logging and monitoring
- [ ] Use environment variables for all secrets
- [ ] Regular security updates for dependencies

### Data Protection
- Passwords are hashed with BCrypt
- JWTs expire after 24 hours (configurable)
- All user data is scoped to authenticated user
- Admin endpoints require ADMIN role
- H2 console disabled in production

---

## File Structure

```
project AI SS/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/scamshield/backend/
│   │   │   │   ├── controller/       # REST endpoints
│   │   │   │   ├── service/          # Business logic
│   │   │   │   ├── repository/       # Data access
│   │   │   │   ├── entity/           # JPA entities
│   │   │   │   ├── dto/              # Data transfer objects
│   │   │   │   ├── config/           # Spring configuration
│   │   │   │   ├── security/         # JWT auth
│   │   │   │   └── exception/        # Error handling
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── schema.sql        # DB initialization
│   │   └── test/
│   ├── pom.xml
│   └── mvnw
│
├── frontend/
│   ├── src/
│   │   ├── pages/          # Page components
│   │   ├── components/     # Reusable components
│   │   ├── context/        # React Context state
│   │   ├── services/       # API client
│   │   ├── App.jsx         # Main routing
│   │   └── index.css       # Tailwind styles
│   ├── public/
│   ├── package.json
│   ├── vite.config.js
│   └── .env.local          # Environment variables
```

---

## Support & Documentation

### Useful Commands

**View Backend Logs:**
```bash
cd backend
mvnw spring-boot:run 2>&1 | tee app.log
```

**Generate Frontend Bundle Report:**
```bash
cd frontend
npm run build -- --analyze
```

**Test API Endpoints:**
```bash
# Health check
curl http://localhost:8080/api/health

# Register user
curl -X POST http://localhost:8080/api/register \
  -H "Content-Type: application/json" \
  -d '{"name":"John","email":"john@test.com","password":"test123"}'

# Login
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john@test.com","password":"test123"}'

# Admin login
curl -X POST http://localhost:8080/api/admin/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin1234"}'

# Scan message (requires JWT token)
curl -X POST http://localhost:8080/api/scan-message \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"text":"Congratulations you won a prize!"}'
```

---

## Performance Optimization

### Backend
- Use connection pooling for database
- Enable query caching for frequently accessed data
- Implement pagination for list endpoints
- Use CDN for static assets

### Frontend
- Code splitting by route
- Lazy load images
- Minify CSS/JS in production build
- Enable gzip compression

---

## Next Steps

1. Set up proper monitoring and logging (e.g., Sentry, ELK Stack)
2. Implement automated testing (Jest for frontend, JUnit for backend)
3. Set up CI/CD pipeline (GitHub Actions, GitLab CI, Jenkins)
4. Configure backup and disaster recovery
5. Plan scaling strategy (load balancing, caching, microservices)
6. Implement advanced admin features (user management, scam report analytics)

---

## License

All rights reserved. Internal use only.

---

**Last Updated**: July 2026
