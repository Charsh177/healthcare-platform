# Quick Start Guide - Healthcare Platform

## 5-Minute Setup

### Option 1: Docker Compose (Easiest)

```bash
# 1. Clone repository
git clone <repo-url>
cd healthcare-platform

# 2. Build and start
docker-compose build
docker-compose up -d

# 3. Access services
echo "API Gateway: http://localhost:8080"
echo "Swagger UI: http://localhost:8080/swagger-ui.html"
echo "Database: localhost:5432"

# 4. Check status
docker-compose ps
```

### Option 2: Local Development

```bash
# 1. Start PostgreSQL
docker run -d --name healthcare-postgres \
  -e POSTGRES_DB=healthcare_db \
  -e POSTGRES_USER=healthcare_user \
  -e POSTGRES_PASSWORD=healthcare_secure_password_123 \
  -p 5432:5432 \
  postgres:15-alpine

# 2. Build project
mvn clean install -DskipTests

# 3. Run each service in separate terminal
cd auth-service && mvn spring-boot:run
cd patient-service && mvn spring-boot:run
cd claims-service && mvn spring-boot:run
cd billing-service && mvn spring-boot:run
cd file-service && mvn spring-boot:run
cd api-gateway && mvn spring-boot:run
```

## First API Call

### 1. Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin@123"
  }'
```

**Response:**
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "...",
    "expiresIn": 900,
    "tokenType": "Bearer"
  }
}
```

**Save token:** `export TOKEN=<accessToken>`

### 2. Create Patient

```bash
curl -X POST http://localhost:8080/api/v1/patients \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "dob": "1990-05-15",
    "gender": "M",
    "mobile": "+91-9876543210",
    "aadhaarNo": "123456789012",
    "address": "123 Main St"
  }'
```

### 3. Search Patients

```bash
curl -X GET "http://localhost:8080/api/v1/patients/search?query=john" \
  -H "Authorization: Bearer $TOKEN"
```

## Test Data

Default credentials:
- **Username:** admin
- **Password:** admin@123

Available roles:
- `ADMIN` - Full system access
- `DOCTOR` - Patient care & diagnosis
- `RECEPTIONIST` - Patient registration
- `INSURANCE_EXECUTIVE` - Claims review
- `BILLING_STAFF` - Invoice & payment
- `SUPER_ADMIN` - System configuration

## Common Commands

```bash
# View all services
docker-compose ps

# View logs
docker-compose logs -f auth-service

# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v

# Rebuild services
docker-compose build --no-cache

# Access database
docker exec -it healthcare-postgres psql -U healthcare_user -d healthcare_db

# Check API health
curl http://localhost:8080/actuator/health
```

## Directory Structure Quick Reference

```
healthcare-platform/
├── auth-service/           → User login/registration
├── patient-service/        → Patient management
├── claims-service/         → Insurance claims
├── billing-service/        → Invoices & payments
├── file-service/           → Document upload
├── api-gateway/            → Request routing
├── docker-compose.yml      → Service orchestration
├── scripts/
│   ├── deploy.sh           → Production deployment
│   ├── dev-setup.sh        → Dev environment
│   └── cleanup.sh          → Stop all services
└── docs/
    ├── API.md              → API reference
    ├── DEPLOYMENT.md       → Production setup
    └── DEVELOPMENT.md      → Dev guide
```

## Troubleshooting

### Services won't start
```bash
# Check logs
docker-compose logs

# Restart
docker-compose restart

# Full rebuild
docker-compose down -v
docker-compose build --no-cache
docker-compose up -d
```

### Port conflict
Edit `docker-compose.yml`:
```yaml
ports:
  - "9080:8080"  # Change first port
```

### Database connection error
```bash
# Test connection
docker exec -it healthcare-postgres psql -U healthcare_user -d healthcare_db -c "SELECT 1"

# Check network
docker network ls
docker network inspect healthcare-network
```

## Next Steps

1. **Read full documentation:**
   - [README.md](../README.md)
   - [docs/DEVELOPMENT.md](DEVELOPMENT.md)
   - [docs/API.md](API.md)

2. **Explore API:**
   - Visit http://localhost:8080/swagger-ui.html
   - Try endpoints with Swagger UI

3. **Deploy to production:**
   - Follow [docs/DEPLOYMENT.md](DEPLOYMENT.md)
   - Configure environment variables
   - Set up monitoring & backups

4. **Customize:**
   - Add new services
   - Extend database schema
   - Integrate payment gateway

## Support

- **GitHub Issues:** Report bugs
- **Documentation:** Check `/docs` folder
- **Community:** Join healthcare tech community
- **Commercial:** Contact for enterprise support

---

**Happy coding!** 🚀
