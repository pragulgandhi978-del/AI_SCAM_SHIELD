# AI Scam Shield - Verification Checklist

Use this checklist to verify that all components are properly installed and configured.

---

## Pre-Flight Checks

### System Requirements
- [ ] Java 21 or higher installed: `java -version`
- [ ] Node.js 18 or higher installed: `node --version`
- [ ] npm 9 or higher installed: `npm --version`
- [ ] Git installed (optional): `git --version`
- [ ] Ports 8080 and 5173 are available (not in use)

### Project Files Exist
- [ ] Backend folder exists: `d:\project AI SS\backend\`
- [ ] Frontend folder exists: `d:\project AI SS\frontend\`
- [ ] `.env.local` exists in frontend: `d:\project AI SS\frontend\.env.local`
- [ ] `application.properties` exists: `d:\project AI SS\backend\src\main\resources\application.properties`
- [ ] `schema.sql` exists: `d:\project AI SS\backend\src\main\resources\schema.sql`

---

## Backend Setup Verification

### Build Backend
```bash
cd backend
mvnw clean package -DskipTests
```

- [ ] Build completes without errors
- [ ] JAR file created: `backend/target/backend-0.0.1-SNAPSHOT.jar`

### Start Backend
```bash
cd backend
mvnw spring-boot:run
```

Wait 30-60 seconds for startup...

- [ ] Backend starts without errors
- [ ] Console shows "Started BackendApplication"
- [ ] No exceptions in startup logs

### Test Backend Health
```bash
curl http://localhost:8080/api/health
```

Expected response:
```json
{"status":"ok"}
```

- [ ] Health check returns 200 OK
- [ ] Response contains "status": "ok"

### Test Default Admin
```bash
curl -X POST http://localhost:8080/api/admin/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin1234"}'
```

Expected response: `{"token":"...", "admin":{"id":1,"username":"admin"}}`

- [ ] Admin login succeeds
- [ ] JWT token is returned
- [ ] User ID is 1

### Test User Registration
```bash
curl -X POST http://localhost:8080/api/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","password":"test123"}'
```

Expected response: `{"token":"...", "user":{"id":1,"name":"Test User","email":"test@example.com","role":"USER"}}`

- [ ] Registration succeeds
- [ ] JWT token is returned
- [ ] User is created with role "USER"

### Test H2 Database Console
Visit: http://localhost:8080/h2-console

- [ ] H2 console page loads
- [ ] JDBC URL field shows: `jdbc:h2:mem:scamshielddb`
- [ ] Can connect with username: `sa`
- [ ] Can run SQL: `SELECT * FROM users;`

---

## Frontend Setup Verification

### Install Dependencies
```bash
cd frontend
npm install
```

- [ ] npm install completes without errors
- [ ] `node_modules` folder created
- [ ] `package-lock.json` exists

### Start Development Server
```bash
cd frontend
npm run dev
```

Wait 10-20 seconds for startup...

- [ ] Development server starts without errors
- [ ] Console shows "Local: http://localhost:5173"
- [ ] No webpack/vite errors

### Test Frontend in Browser
Visit: http://localhost:5173

- [ ] Page loads without blank/error screens
- [ ] AI Scam Shield logo is visible
- [ ] Page redirects to login after 1.5 seconds
- [ ] Browser console has no errors (F12 → Console)

### Test Frontend Registration
On login page:
- [ ] Click "Create one" link to go to register
- [ ] Fill form with:
  - Name: Test User
  - Email: test@example.com
  - Password: test123456
  - Confirm: test123456
- [ ] Click "Register"

- [ ] Registration succeeds
- [ ] Redirects to dashboard
- [ ] User greeting shows "Welcome Test User"
- [ ] No errors in console

### Test Scan Message
On dashboard:
- [ ] Click "Scan Message" in navbar
- [ ] Enter test message: "Congratulations you won $1M! Click here!"
- [ ] Click "Scan message"

- [ ] Request sent to backend (check Network tab)
- [ ] Response received without 503 error
- [ ] Risk level badge displays (if APIs configured)
- [ ] Loading spinner appears during scan

### Test Dashboard Stats
- [ ] Dashboard loads
- [ ] Shows "0" scans initially (first run)
- [ ] After a scan, numbers increment
- [ ] Stats display: Total Scans, Safe, Scam, Recent Risk

### Test Scan History
- [ ] Click "History" in navbar
- [ ] History page loads
- [ ] Shows scans from previous tests
- [ ] Pagination works (if multiple scans)
- [ ] Clicking row shows details

### Test Logout
- [ ] Click "Log out" button
- [ ] Redirects to login page
- [ ] Browser localStorage cleared (DevTools → Application)
- [ ] Cannot access dashboard with back button

---

## Frontend-Backend Integration

### CORS Configuration
- [ ] Backend allows origin: `http://localhost:5173`
- [ ] Frontend requests include Authorization header
- [ ] No CORS errors in browser console

### API Communication
- [ ] Frontend makes requests to `http://localhost:8080/api`
- [ ] JWT tokens are sent correctly
- [ ] 401 responses trigger logout
- [ ] Error messages display properly

### Session Management
- [ ] JWT tokens stored in localStorage
- [ ] Token sent with every authenticated request
- [ ] Logout clears tokens
- [ ] App redirects unauthenticated users to login

---

## Environment Configuration

### Backend Configuration File
File: `backend/src/main/resources/application.properties`

- [ ] Server port is 8080
- [ ] H2 database URL set correctly
- [ ] JPA DDL mode is "none"
- [ ] SQL initialization is enabled
- [ ] CORS is configured

### Frontend Configuration File
File: `frontend/.env.local`

- [ ] VITE_API_URL is set to `http://localhost:8080/api`
- [ ] File has no syntax errors

### Environment Variables
Option 1: Optional for development (APIs return 503)
Option 2: For testing with real APIs:
- [ ] OPENAI_API_KEY set (if testing message scan)
- [ ] SAFE_BROWSING_API_KEY set (if testing URL scan)
- [ ] JWT_SECRET set (recommended)

---

## Database Verification

### Tables Created
Via H2 console, run:
```sql
SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES;
```

- [ ] `users` table exists
- [ ] `admins` table exists
- [ ] `scan_history` table exists
- [ ] `scam_reports` table exists

### Sample Data
```sql
SELECT * FROM users;
SELECT * FROM admins;
```

- [ ] Admin user exists with username: `admin`
- [ ] Test user exists from registration test

---

## Performance Checks

### Backend Response Time
When accessing http://localhost:8080/api/health:

- [ ] Response time < 100ms
- [ ] Status code: 200
- [ ] No memory errors in logs

### Frontend Page Load
When accessing http://localhost:5173:

- [ ] Page loads in < 3 seconds
- [ ] All assets load (no 404s in Network tab)
- [ ] No JavaScript errors in console

### Database Query Performance
Via H2 console:

- [ ] `SELECT COUNT(*) FROM users;` returns < 100ms
- [ ] Pagination queries return < 100ms

---

## Security Checks

### Password Hashing
```sql
SELECT username, password FROM admins WHERE username='admin';
```

- [ ] Password is hashed (starts with `$2a$`)
- [ ] Not plain text "admin1234"

### JWT Token Format
After login, check token in localStorage:

```javascript
// In browser console
localStorage.getItem('scamshield_token')
```

- [ ] Token starts with `eyJ` (Base64 encoded)
- [ ] Token has 3 parts separated by dots
- [ ] Token is not empty

### CORS Security
- [ ] CORS only allows specific origins
- [ ] Not wildcard `*` for all origins
- [ ] Credentials are handled correctly

---

## Advanced Verification

### Rate Limiting Test (if API keys configured)
- [ ] Make 11 scan requests rapidly
- [ ] 11th request returns 429 Too Many Requests
- [ ] Error message: "Rate limit exceeded"

### Admin Authorization Test
- [ ] Non-admin cannot access `/api/admin/users`
- [ ] Returns 403 Forbidden
- [ ] Admin can access and sees private data

### Data Persistence Test (H2)
- [ ] Stop backend (Ctrl+C)
- [ ] Start backend again
- [ ] Data from before restart is GONE (H2 is in-memory)
- [ ] Admin account is recreated (from DataInitializer)

### Data Persistence Test (PostgreSQL, if migrated)
- [ ] Stop backend
- [ ] Start backend
- [ ] Data persists from before stop

---

## Common Test Scenarios

### Scenario 1: Full User Journey
1. [ ] Visit app → Splash screen shows
2. [ ] Redirected to login
3. [ ] Click register
4. [ ] Fill in details and submit
5. [ ] Token received and stored
6. [ ] Dashboard loads with 0 scans
7. [ ] Scan a message
8. [ ] Result displays
9. [ ] View history
10. [ ] Logout
11. [ ] Cannot access dashboard
12. [ ] Can login again

### Scenario 2: Admin Features
1. [ ] Admin login succeeds
2. [ ] Frontend doesn't show admin links (frontend only has user features)
3. [ ] Admin can call `/api/admin/users` endpoint
4. [ ] Admin can call `/api/admin/reports` endpoint

### Scenario 3: Error Handling
1. [ ] Invalid email on login → Error message shows
2. [ ] Wrong password → Error message shows
3. [ ] Offline API → Shows "service unavailable"
4. [ ] Network error → Shows error message
5. [ ] Expired token → Auto-logout and redirect to login

---

## Final Verification

### Documentation
- [ ] README.md exists and is readable
- [ ] SETUP_AND_DEPLOYMENT.md exists
- [ ] API_DOCUMENTATION.md exists
- [ ] PROJECT_COMPLETION_SUMMARY.md exists

### Quick Start Scripts
- [ ] start_all.bat exists (Windows)
- [ ] start_all.sh exists (Linux/macOS)
- [ ] Scripts are executable

### Production Readiness
- [ ] No hardcoded passwords except dev defaults
- [ ] No console.log() statements left in production code
- [ ] No sensitive data in error messages
- [ ] All input is validated
- [ ] Database has indexes (auto-generated)

---

## Sign-Off

When all items are checked, the project is verified and ready!

Date Verified: ___________
Verified By: ___________
Notes: ___________

---

## Troubleshooting

If any check fails, refer to:
1. `SETUP_AND_DEPLOYMENT.md` - Troubleshooting section
2. `API_DOCUMENTATION.md` - Error codes and examples
3. Backend console logs
4. Browser DevTools (F12)
5. H2 console for database issues

---

Created: July 5, 2026
