# Healthcare Platform - API Documentation

## Base URL

**Local Development:** `http://localhost:8080`  
**Production:** `https://healthcareplatform.com`

## Authentication

All endpoints (except login/register) require JWT Bearer token:

```
Authorization: Bearer <access_token>
```

---

## Authentication Service

### 1. User Login

**Endpoint:** `POST /api/v1/auth/login`

**Request Body:**
```json
{
  "username": "admin",
  "password": "admin@123"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
    "expiresIn": 900,
    "tokenType": "Bearer"
  },
  "message": "Login successful"
}
```

**Error (401 Unauthorized):**
```json
{
  "success": false,
  "error": "Invalid credentials"
}
```

---

### 2. User Registration

**Endpoint:** `POST /api/v1/auth/register`

**Request Body:**
```json
{
  "username": "doctor1",
  "password": "SecurePass@123",
  "email": "doctor1@hospital.com",
  "roleId": "b2c3d4e5-f6a7-4890-c1d2-e3f4a5b6c7d8"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "data": {
    "userId": "uuid",
    "username": "doctor1",
    "email": "doctor1@hospital.com",
    "roleId": "b2c3d4e5-f6a7-4890-c1d2-e3f4a5b6c7d8",
    "active": true,
    "createdAt": "2026-06-11T10:30:00"
  },
  "message": "User registered successfully"
}
```

---

### 3. Refresh Token

**Endpoint:** `POST /api/v1/auth/refresh`

**Headers:**
```
Authorization: Bearer <refresh_token>
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "accessToken": "new_token...",
    "refreshToken": "refresh_token...",
    "expiresIn": 900,
    "tokenType": "Bearer"
  }
}
```

---

## Patient Service

### 1. Create Patient

**Endpoint:** `POST /api/v1/patients`

**Headers:**
```
Authorization: Bearer <access_token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "dob": "1990-05-15",
  "gender": "M",
  "mobile": "+91-9876543210",
  "aadhaarNo": "123456789012",
  "address": "123 Main St, City"
}
```

**Response (201 Created):**
```json
{
  "patientId": "uuid",
  "uhid": "UH-1623401400000-ABCDEF",
  "firstName": "John",
  "lastName": "Doe",
  "dob": "1990-05-15",
  "gender": "M",
  "mobile": "+91-9876543210",
  "aadhaarNo": "123456789012",
  "address": "123 Main St, City",
  "createdAt": "2026-06-11T10:30:00"
}
```

---

### 2. Get Patient by ID

**Endpoint:** `GET /api/v1/patients/{id}`

**Parameters:**
- `id` (path): Patient UUID

**Response (200 OK):**
```json
{
  "patientId": "uuid",
  "uhid": "UH-1623401400000-ABCDEF",
  "firstName": "John",
  "lastName": "Doe",
  "dob": "1990-05-15",
  "gender": "M",
  "mobile": "+91-9876543210",
  "aadhaarNo": "123456789012",
  "address": "123 Main St, City",
  "createdAt": "2026-06-11T10:30:00"
}
```

---

### 3. Get Patient by UHID

**Endpoint:** `GET /api/v1/patients/uhid/{uhid}`

**Parameters:**
- `uhid` (path): Unique Health ID

**Response (200 OK):**
Same as above

---

### 4. Update Patient

**Endpoint:** `PUT /api/v1/patients/{id}`

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "dob": "1990-05-15",
  "gender": "M",
  "mobile": "+91-9876543210",
  "address": "New Address"
}
```

**Response (200 OK):**
Updated patient object

---

### 5. Search Patients

**Endpoint:** `GET /api/v1/patients/search`

**Query Parameters:**
- `query` (required): Search term (name, UHID, mobile)

**Response (200 OK):**
```json
[
  {
    "patientId": "uuid",
    "uhid": "UH-1623401400000-ABCDEF",
    "firstName": "John",
    "lastName": "Doe",
    ...
  }
]
```

---

## Claims Service

### 1. Create Claim

**Endpoint:** `POST /api/v1/claims`

**Request Body:**
```json
{
  "patientId": "uuid",
  "insuranceCompany": "ABC Insurance",
  "tpaName": "TPA Corp",
  "claimType": "PREAUTH",
  "preAuthAmount": 50000,
  "admissionDate": "2026-06-10",
  "dischargeDate": "2026-06-15"
}
```

**Response (201 Created):**
```json
{
  "claimId": "uuid",
  "patientId": "uuid",
  "claimNo": "CLM-20260611-001",
  "insuranceCompany": "ABC Insurance",
  "claimStatus": "DRAFT",
  "preAuthAmount": 50000,
  "createdAt": "2026-06-11T10:30:00"
}
```

---

### 2. Get Claim by ID

**Endpoint:** `GET /api/v1/claims/{id}`

**Response (200 OK):**
Full claim object with status

---

### 3. Update Claim

**Endpoint:** `PUT /api/v1/claims/{id}`

**Request Body:**
```json
{
  "claimStatus": "PREAUTH_APPROVED",
  "approvedAmount": 50000
}
```

**Response (200 OK):**
Updated claim object

---

### 4. Upload Claim Documents

**Endpoint:** `POST /api/v1/claims/{id}/documents`

**Content-Type:** `multipart/form-data`

**Parameters:**
- `id` (path): Claim ID
- `files` (form): Multiple files (PDF, JPG, PNG, TIFF)

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "claimId": "uuid",
    "documentsUploaded": 3,
    "totalDocuments": 5
  },
  "message": "Documents uploaded successfully"
}
```

---

### 5. Filter Claims by Status

**Endpoint:** `GET /api/v1/claims/status/{status}`

**Path Parameters:**
- `status`: DRAFT, PREAUTH_PENDING, PREAUTH_APPROVED, UNDER_REVIEW, APPROVED, SETTLED

**Response (200 OK):**
```json
[
  {
    "claimId": "uuid",
    "claimNo": "CLM-20260611-001",
    "claimStatus": "PREAUTH_APPROVED",
    ...
  }
]
```

---

## Billing Service

### 1. Create Invoice

**Endpoint:** `POST /api/v1/billing/invoice`

**Request Body:**
```json
{
  "patientId": "uuid",
  "amount": 15000,
  "gst": 2700,
  "paymentMode": "CASH"
}
```

**Response (201 Created):**
```json
{
  "invoiceId": "uuid",
  "invoiceNo": "INV-20260611-001",
  "patientId": "uuid",
  "amount": 15000,
  "gst": 2700,
  "totalAmount": 17700,
  "paymentMode": "CASH",
  "createdAt": "2026-06-11T10:30:00"
}
```

---

### 2. Get Invoice

**Endpoint:** `GET /api/v1/billing/invoice/{id}`

**Response (200 OK):**
Full invoice object

---

### 3. Record Payment

**Endpoint:** `POST /api/v1/billing/payment`

**Request Body:**
```json
{
  "invoiceId": "uuid",
  "paymentMode": "BANK_TRANSFER",
  "amount": 17700,
  "referenceNo": "BANK-12345"
}
```

**Response (200 OK):**
```json
{
  "paymentId": "uuid",
  "invoiceId": "uuid",
  "amount": 17700,
  "paymentMode": "BANK_TRANSFER",
  "referenceNo": "BANK-12345",
  "paymentDate": "2026-06-11T10:30:00"
}
```

---

## File Service

### 1. Upload File

**Endpoint:** `POST /api/v1/files/upload`

**Content-Type:** `multipart/form-data`

**Parameters:**
- `file` (form): File to upload
- `claimId` (query): Associated claim ID
- `docType` (query): Document type (PRESCRIPTION, BILL, REPORT)

**Response (200 OK):**
```json
{
  "fileId": "uuid",
  "originalName": "prescription.pdf",
  "storedName": "file-1623401400000.pdf",
  "mimeType": "application/pdf",
  "size": 102400,
  "uploadTime": "2026-06-11T10:30:00",
  "uploadedBy": "doctor1"
}
```

---

### 2. Download File

**Endpoint:** `GET /api/v1/files/{id}`

**Response (200 OK):**
Binary file stream

---

### 3. Delete File

**Endpoint:** `DELETE /api/v1/files/{id}`

**Response (204 No Content)**

---

## Error Responses

### 400 Bad Request
```json
{
  "success": false,
  "error": "Validation failed: Email is required"
}
```

### 401 Unauthorized
```json
{
  "success": false,
  "error": "Invalid or expired token"
}
```

### 403 Forbidden
```json
{
  "success": false,
  "error": "You don't have permission for this resource"
}
```

### 404 Not Found
```json
{
  "success": false,
  "error": "Patient not found"
}
```

### 409 Conflict
```json
{
  "success": false,
  "error": "Patient with this mobile number already exists"
}
```

### 500 Internal Server Error
```json
{
  "success": false,
  "error": "An unexpected error occurred"
}
```

---

## Rate Limiting

All endpoints are rate-limited to prevent abuse:
- **Standard:** 1000 requests/hour per IP
- **Premium:** 10000 requests/hour per API key

Response Header: `X-RateLimit-Remaining`

---

## Pagination

List endpoints support pagination:

```
GET /api/v1/patients?page=0&size=20&sort=createdAt,desc
```

Response includes:
```json
{
  "content": [...],
  "page": 0,
  "size": 20,
  "totalElements": 150,
  "totalPages": 8
}
```

---

## Testing with cURL

```bash
# Login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin@123"}'

# Create Patient
curl -X POST http://localhost:8080/api/v1/patients \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d @patient.json

# Search Patients
curl -X GET "http://localhost:8080/api/v1/patients/search?query=john" \
  -H "Authorization: Bearer <token>"
```

---

For detailed API documentation, visit: **http://localhost:8080/swagger-ui.html**

