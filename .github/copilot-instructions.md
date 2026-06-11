# Healthcare Platform - Development Workspace Guide

## Overview
This is a complete end-to-end healthcare claim processing platform built with:
- **Java 17** + **Spring Boot 3**
- **PostgreSQL** for data persistence
- **Docker & Docker Compose** for containerization
- **Modular Microservices Architecture**
- **JWT Authentication & RBAC**

## Project Structure

```
healthcare-platform/
├── 6 Microservices (auth, patient, claims, billing, file, api-gateway)
├── Common Library (shared utilities)
├── PostgreSQL Database (containerized)
├── Docker Compose (orchestration)
├── Comprehensive Documentation
└── Deployment Scripts
```

## Quick Commands

### Getting Started
```bash
docker-compose build && docker-compose up -d
curl http://localhost:8080/actuator/health
```

### Development
```bash
mvn clean install -DskipTests
cd <service> && mvn spring-boot:run
```

### Maintenance
```bash
docker-compose logs -f <service>
docker-compose restart
docker-compose down -v  # Stop & cleanup
```

## Key Features (Phase 1)
✅ User authentication & authorization  
✅ Patient lifecycle management  
✅ Insurance claim processing  
✅ Invoice & billing management  
✅ Document upload & storage  
✅ REST APIs with Swagger documentation  

## Important Files

| File | Purpose |
|------|---------|
| `docker-compose.yml` | Service orchestration |
| `pom.xml` | Maven dependencies |
| `README.md` | Project overview |
| `docs/API.md` | API reference |
| `docs/DEPLOYMENT.md` | Production deployment |
| `docs/DEVELOPMENT.md` | Dev environment setup |
| `docs/QUICKSTART.md` | 5-minute quick start |
| `scripts/deploy.sh` | Automated deployment |

## Architecture
- **API Gateway (8080)** → Routes to microservices
- **Auth Service (8081)** → JWT authentication
- **Patient Service (8082)** → Patient records
- **Claims Service (8083)** → Insurance claims
- **Billing Service (8084)** → Invoices & payments
- **File Service (8085)** → Document management
- **PostgreSQL (5432)** → Central database

## Default Credentials
- Username: `admin`
- Password: `admin@123`

## Roles
- `ADMIN` - Full access
- `DOCTOR` - Patient care
- `RECEPTIONIST` - Patient registration
- `INSURANCE_EXECUTIVE` - Claims review
- `BILLING_STAFF` - Invoicing
- `SUPER_ADMIN` - System admin

## Documentation
- 📖 [API Reference](docs/API.md)
- 🚀 [Quick Start](docs/QUICKSTART.md)
- 💻 [Development Guide](docs/DEVELOPMENT.md)
- 🔧 [Deployment Guide](docs/DEPLOYMENT.md)
- 📋 [README](README.md)

## Testing the System

### Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin@123"}'
```

### Create Patient
```bash
curl -X POST http://localhost:8080/api/v1/patients \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe","dob":"1990-05-15",...}'
```

## Technology Stack
| Layer | Technology |
|-------|-----------|
| Backend | Java 17, Spring Boot 3 |
| ORM | Hibernate/JPA |
| Database | PostgreSQL 15 |
| Security | JWT, Spring Security |
| API Documentation | Swagger OpenAPI |
| Containerization | Docker |
| Build | Maven |
| Logging | SLF4J |

## Best Practices Implemented
✅ Microservices architecture  
✅ REST API standards  
✅ Database schema organization  
✅ JWT-based authentication  
✅ Role-based access control  
✅ Exception handling  
✅ Logging & monitoring  
✅ Docker containerization  
✅ API documentation  
✅ Clean code standards  

## Next Steps
1. Review [QUICKSTART.md](docs/QUICKSTART.md) for 5-minute setup
2. Explore [API.md](docs/API.md) for available endpoints
3. Follow [DEVELOPMENT.md](docs/DEVELOPMENT.md) for adding features
4. Check [DEPLOYMENT.md](docs/DEPLOYMENT.md) for production setup

## Support & Resources
- **Documentation:** See `/docs` folder
- **Issues:** GitHub Issues
- **API Docs:** Swagger UI (http://localhost:8080/swagger-ui.html)
- **Database:** PostgreSQL with UUID primary keys
- **File Storage:** Local filesystem (extensible to S3)

---

**For detailed setup instructions, see [README.md](../README.md)**
