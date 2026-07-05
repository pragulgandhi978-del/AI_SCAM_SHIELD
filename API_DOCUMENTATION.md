# AI Scam Shield - API Documentation

## Base URL

**Development**: `http://localhost:8080/api`
**Production**: `https://api.yourdomain.com/api`

---

## Authentication

### JWT Token Format

All protected endpoints require an `Authorization` header:

```
Authorization: Bearer <jwt_token>
```

**Token Lifetime**: 24 hours (configurable via `JWT_EXPIRATION_MS`)

---

## Public Endpoints

### 1. Health Check
**GET** `/health`

Check if the API is running.

**Response (200 OK):**
```json
{
  "status": "ok"
}
```

No authentication required.

---

### 2. User Registration
**POST** `/register`

Register a new user account.

**Request:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "securePassword123"
}
```

**Validation:**
- `name`: Required, max 100 characters
- `email`: Required, valid email format, must be unique
- `password`: Required, min 6 characters

**Response (201 Created):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "USER"
  }
}
```

---

### 3. User Login
**POST** `/login`

Authenticate with email and password.

**Request:**
```json
{
  "email": "john@example.com",
  "password": "securePassword123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "USER"
  }
}
```

**Error (401 Unauthorized):**
```json
{
  "error": "Invalid email or password"
}
```

---

### 4. Admin Login
**POST** `/admin/login`

Authenticate as administrator.

**Request:**
```json
{
  "username": "admin",
  "password": "admin1234"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "admin": {
    "id": 1,
    "username": "admin"
  }
}
```

**Default Admin Credentials** (must be changed in production):
- Username: `admin`
- Password: `admin1234`

---

## Protected Endpoints (Authentication Required)

### 5. Get Dashboard Stats
**GET** `/dashboard`

Get scan statistics for the authenticated user.

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response (200 OK):**
```json
{
  "totalScans": 42,
  "safeCount": 35,
  "scamCount": 7,
  "recentRiskLevel": "Medium"
}
```

---

### 6. Scan Message
**POST** `/scan-message`

Analyze a text message for scam risk using OpenAI.

**Headers:**
```
Authorization: Bearer <jwt_token>
Content-Type: application/json
```

**Request:**
```json
{
  "text": "Congratulations! You've won $1,000,000. Click here to claim your prize now!"
}
```

**Validation:**
- `text`: Required, max 2000 characters

**Response (200 OK):**
```json
{
  "riskLevel": "High",
  "explanation": "This message contains classic scam indicators: unsolicited prize notification, urgency creation, and suspicious call-to-action."
}
```

**Risk Levels:** `Low` | `Medium` | `High`

**Error (429 Too Many Requests):**
```json
{
  "error": "Rate limit exceeded, try again later"
}
```
> Per-user limit: 10 scans per hour

**Error (503 Service Unavailable):**
```json
{
  "error": "Scan service temporarily unavailable, please try again"
}
```
> OpenAI API is down or credentials are invalid

---

### 7. Scan URL
**POST** `/scan-url`

Check a URL against known malicious sites using Google Safe Browsing.

**Headers:**
```
Authorization: Bearer <jwt_token>
Content-Type: application/json
```

**Request:**
```json
{
  "url": "https://example-phishing.com"
}
```

**Validation:**
- `url`: Required, must start with `http://` or `https://`

**Response (200 OK):**
```json
{
  "status": "Safe"
}
```

**Status Values:** `Safe` | `Malicious`

**Error (429 Too Many Requests):**
```json
{
  "error": "Rate limit exceeded, try again later"
}
```

**Error (503 Service Unavailable):**
```json
{
  "error": "Scan service temporarily unavailable, please try again"
}
```

---

### 8. Get Scan History
**GET** `/history?page=0&size=10`

Get paginated scan history for the authenticated user.

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Query Parameters:**
- `page`: 0-based page number (default: 0)
- `size`: Items per page, max 50 (default: 10)

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 123,
      "contentType": "MESSAGE",
      "contentText": "Congratulations you won...",
      "riskScore": "High",
      "explanation": "Classic scam indicators...",
      "scannedAt": "2026-07-05T14:30:00"
    },
    {
      "id": 122,
      "contentType": "URL",
      "contentText": "https://example.com",
      "riskScore": "Safe",
      "explanation": null,
      "scannedAt": "2026-07-05T14:25:00"
    }
  ],
  "totalPages": 5,
  "totalElements": 42,
  "currentPage": 0
}
```

---

### 9. Report a Scam
**POST** `/report-scam`

Report a suspected scam for administrative review.

**Headers:**
```
Authorization: Bearer <jwt_token>
Content-Type: application/json
```

**Request:**
```json
{
  "contentText": "Suspicious message or URL",
  "category": "Phishing"
}
```

**Validation:**
- `contentText`: Required, non-empty
- `category`: Required, one of: `Phishing`, `Fake Job`, `Lottery Scam`, `Other`

**Response (201 Created):**
```json
{
  "message": "Report submitted successfully"
}
```

---

## Admin-Only Endpoints

### 10. Get All Users
**GET** `/admin/users`

Get a list of all registered users (admin only).

**Headers:**
```
Authorization: Bearer <admin_jwt_token>
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "USER",
    "createdAt": "2026-07-01T10:00:00"
  },
  {
    "id": 2,
    "name": "Jane Smith",
    "email": "jane@example.com",
    "role": "USER",
    "createdAt": "2026-07-02T15:30:00"
  }
]
```

**Error (403 Forbidden):**
```json
{
  "error": "Access denied"
}
```
> Only admin role can access this endpoint

---

### 11. Get All Scam Reports
**GET** `/admin/reports`

Get a list of all reported scams (admin only).

**Headers:**
```
Authorization: Bearer <admin_jwt_token>
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "userId": 5,
    "contentText": "Suspicious content...",
    "category": "Phishing",
    "status": "PENDING",
    "reportedAt": "2026-07-05T14:30:00"
  },
  {
    "id": 2,
    "userId": 3,
    "contentText": "Fake job offer...",
    "category": "Fake Job",
    "status": "PENDING",
    "reportedAt": "2026-07-05T13:15:00"
  }
]
```

**Report Status:** `PENDING` | `REVIEWED` | `CLOSED`

---

## Error Responses

### 400 Bad Request
```json
{
  "error": "Validation failed; Email must be a valid email address"
}
```

### 401 Unauthorized
```json
{
  "error": "Unauthorized"
}
```
> Missing or invalid JWT token

### 403 Forbidden
```json
{
  "error": "Access denied"
}
```
> Insufficient permissions (e.g., non-admin accessing admin endpoint)

### 429 Too Many Requests
```json
{
  "error": "Rate limit exceeded, try again later"
}
```
> 10 scans per hour per user

### 500 Internal Server Error
```json
{
  "error": "An unexpected error occurred. Please try again later."
}
```

---

## Rate Limiting

Per-user rate limits:
- **Scan Message**: 10 per hour
- **Scan URL**: 10 per hour (counted together)

Rate limit is per-user, per 1-hour rolling window.

**Headers in Response:**
```
X-RateLimit-Limit: 10
X-RateLimit-Remaining: 7
X-RateLimit-Reset: 1625334600
```

---

## Examples

### Example 1: Full User Flow

```bash
# 1. Register
curl -X POST http://localhost:8080/api/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "test123456"
  }'

# Save the token from response

TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

# 2. Get Dashboard
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/dashboard

# 3. Scan a Message
curl -X POST http://localhost:8080/api/scan-message \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"text": "You won a prize!"}'

# 4. Scan a URL
curl -X POST http://localhost:8080/api/scan-url \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"url": "https://example.com"}'

# 5. Get History
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8080/api/history?page=0&size=10"
```

### Example 2: Admin Flow

```bash
# 1. Admin Login
curl -X POST http://localhost:8080/api/admin/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin1234"}'

# Save the token

TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

# 2. Get All Users
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/admin/users

# 3. Get All Reports
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/admin/reports
```

---

## API Rate Limiting Strategy

The API implements a token-bucket rate limiter per user:

```
Limit: 10 scans per hour
Window: 1 hour rolling window
Strategy: Token bucket with in-memory storage
```

When rate limit is exceeded, the API returns a 429 response.

---

## CORS Configuration

The API is configured to accept requests from:
- http://localhost:5173 (development)
- http://localhost:3000 (alternative dev port)
- http://127.0.0.1:5173

**Allowed Methods:** GET, POST, PUT, DELETE, OPTIONS
**Allowed Headers:** * (all headers)
**Max Age:** 1 hour

---

## Testing with Postman

1. Create a new collection
2. Import requests from examples above
3. Set `{{baseUrl}}` variable to `http://localhost:8080/api`
4. Set `{{token}}` variable after login/register
5. Use `Bearer {{token}}` in Authorization header for protected endpoints

---

Created: July 2026
