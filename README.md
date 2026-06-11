# Healthcare Claim Processing & Hospital Workflow Platform

A comprehensive, modular, and scalable healthcare platform for managing patient lifecycle, insurance claims processing, hospital workflows, and billing operations.

## Overview

**Version:** 1.0.0  
**Target Users:** Small hospitals, rural healthcare centers, diagnostic clinics, nursing homes  
**Architecture:** Modular Monolith → Microservices  
**Technology Stack:** Java 17, Spring Boot 3, PostgreSQL, Docker, React

## Key Features

### Phase 1 (MVP)
- ✅ User Authentication & Authorization (JWT-based)
- ✅ Patient Management (Registration, Search, Visit History)
- ✅ Claims Processing (CRUD, Lifecycle Management)
- ✅ File Upload & Document Management
- ✅ OPD & IPD Billing
- ✅ Invoice Generation

### Phase 2+ (Future)
- [ ] Advanced Reports & Analytics
- [ ] Claim Workflows & Notifications
- [ ] OCR Processing
- [ ] Payment Gateway Integration
- [ ] Telemedicine Support

## Tech Stack

| Component | Technology |
|-----------|-----------|
| **Backend Language** | Java 17 |
| **Framework** | Spring Boot 3.2 |
| **Security** | Spring Security + JWT |
| **ORM** | Hibernate/JPA |
| **Database** | PostgreSQL 15 |
| **API Docs** | Swagger OpenAPI 3.0 |
| **Containerization** | Docker & Docker Compose |
| **Frontend** | React 18 (Optional) |
| **Build Tool** | Maven |
| **File Storage** | Local (Extensible to S3) |

## Project Structure

```
healthcare-platform/
├── api-gateway/                 # API Gateway (Spring Cloud Gateway)
├── auth-service/                # Authentication & Authorization
├── patient-service/             # Patient Management
├── claims-service/              # Claims Processing
├── billing-service/             # Billing & Invoicing
├── file-service/                # File Upload & Storage
├── common-lib/                  # Shared Libraries & Utils
├── frontend/                    # React Frontend (Optional)
├── docker/                      # Docker configurations
├── scripts/                     # Deployment & utility scripts
├── docs/                        # Documentation & guides
├── pom.xml                      # Root Maven POM
└── docker-compose.yml           # Docker Compose orchestration
```

## Getting Started

### Prerequisites

- **Docker & Docker Compose** (v24+)
- **Maven** (v3.8+) - for local development
- **Java 17** - for local development
- **Git**

### Quick Start (Using Docker)

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd healthcare-platform
   ```

2. **Build all services:**
   ```bash
   docker-compose build
   ```

3. **Start all services:**
   ```bash
   docker-compose up -d
   ```

4. **Access the services:**
   - API Gateway: http://localhost:8080
   - Auth Service: http://localhost:8081
   - Patient Service: http://localhost:8082
   - Claims Service: http://localhost:8083
   - Billing Service: http://localhost:8084
   - File Service: http://localhost:8085
   - Swagger UI: http://localhost:8080/swagger-ui.html

5. **Check service health:**
   ```bash
   curl http://localhost:8080/actuator/health
   ```

6. **View logs:**
   ```bash
   docker-compose logs -f <service-name>
   ```

### Local Development

1. **Setup PostgreSQL:**
   ```bash
   docker-compose up postgres -d
   ```

2. **Build all services:**
   ```bash
   mvn clean install -DskipTests
   ```

3. **Run individual service:**
   ```bash
   cd auth-service
   mvn spring-boot:run
   ```

## API Documentation

### Authentication Endpoints

```
POST   /api/v1/auth/login          - User login
POST   /api/v1/auth/register       - User registration
POST   /api/v1/auth/refresh        - Token refresh
GET    /api/v1/auth/users          - List users
```

### Patient Management

```
POST   /api/v1/patients            - Create patient
GET    /api/v1/patients/{id}       - Get patient
PUT    /api/v1/patients/{id}       - Update patient
GET    /api/v1/patients/search     - Search patients
```

### Claims Processing

```
POST   /api/v1/claims              - Create claim
GET    /api/v1/claims/{id}         - Get claim
PUT    /api/v1/claims/{id}         - Update claim
POST   /api/v1/claims/{id}/docs    - Upload documents
GET    /api/v1/claims/status/{status} - Filter by status
```

### Billing

```
POST   /api/v1/billing/invoice     - Create invoice
GET    /api/v1/billing/invoice/{id} - Fetch invoice
POST   /api/v1/billing/payment     - Record payment
```

### File Management

```
POST   /api/v1/files/upload        - Upload file
GET    /api/v1/files/{id}          - Download file
DELETE /api/v1/files/{id}          - Delete file
```

## Database Schema

The platform uses PostgreSQL with 5 schemas:

- `auth_schema` - User & role management
- `patient_schema` - Patient & admission data
- `billing_schema` - Billing & invoice data
- `claim_schema` - Claims & documents
- `file_schema` - File metadata

Run migrations automatically on service startup via Liquibase or Flyway.

## Security

- **Authentication:** JWT with access + refresh tokens
- **Authorization:** Role-Based Access Control (RBAC)
- **Encryption:** Password hashing with BCrypt
- **Transport:** HTTPS/TLS (production)
- **Audit:** All critical operations logged

### User Roles

- `ADMIN` - Full system access
- `DOCTOR` - Patient care & diagnosis
- `RECEPTIONIST` - Patient registration & scheduling
- `INSURANCE_EXECUTIVE` - Claims review & approval
- `BILLING_STAFF` - Invoice & payment processing
- `SUPER_ADMIN` - System configuration

## Configuration

### Environment Variables

```env
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/healthcare_db
SPRING_DATASOURCE_USERNAME=healthcare_user
SPRING_DATASOURCE_PASSWORD=<secure-password>

# JWT
JWT_SECRET=<your-256bit-secret>
JWT_EXPIRATION=900000
JWT_REFRESH_EXPIRATION=2592000000

# File Upload
FILE_MAX_SIZE=26214400
FILE_UPLOAD_DIR=/app/files

# Logging
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_COM_HEALTHCARE=DEBUG
```

## Deployment

### Docker Compose (Development/Small Deployment)

```bash
docker-compose -f docker-compose.yml up -d
```

### Production Deployment

For production, consider:

- **Kubernetes** - Replace Docker Compose
- **Cloud Hosting** - AWS, Azure, GCP
- **Database** - Managed RDS/PostgreSQL
- **Storage** - S3/Cloud Storage for files
- **SSL/TLS** - SSL certificates via Let's Encrypt
- **Monitoring** - Prometheus + Grafana
- **Logging** - ELK Stack

See `docs/DEPLOYMENT.md` for detailed instructions.

## Development Roadmap

### Phase 1 (2 Months) - MVP
- Authentication system
- Patient management
- Basic claim processing
- File upload functionality
- OPD/IPD billing

### Phase 2 (2 Months)
- Advanced claim workflows
- Notifications (Email, SMS)
- Report generation
- Analytics dashboard

### Phase 3 (2 Months)
- OCR automation
- Fraud detection
- AI-based claim prediction
- Payment gateway integration

## Coding Standards

### Java Conventions

```java
// Classes
public class PatientService { }

// Variables & Methods
private String patientName;
public void registerPatient() { }

// Constants
private static final String STATUS_ACTIVE = "ACTIVE";
```

### REST API

```json
// Success Response
{
  "success": true,
  "data": { },
  "message": "Operation successful"
}

// Error Response
{
  "success": false,
  "error": "Validation error",
  "timestamp": "2026-06-11T10:30:00Z"
}
```

## Testing

```bash
# Run all tests
mvn test

# Run specific service tests
mvn test -pl auth-service

# Run with coverage
mvn test jacoco:report
```

## Troubleshooting

### Services won't start

```bash
# Check logs
docker-compose logs -f

# Verify database connectivity
docker-compose exec postgres psql -U healthcare_user -d healthcare_db

# Restart services
docker-compose restart
```

### Port conflicts

Update `docker-compose.yml`:
```yaml
ports:
  - "9080:8080"  # Change to different host port
```

## Contributing

1. Create feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -am 'Add feature'`
3. Push branch: `git push origin feature/your-feature`
4. Create Pull Request

## Support & Maintenance

- **Issues:** Report via GitHub Issues
- **Documentation:** See `/docs` folder
- **Community:** Healthcare tech discussions
- **License:** Open Source (Apache 2.0)

## References

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [OpenMRS](https://openmrs.org/) - Reference EMR
- [HAPI FHIR](http://hapifhir.io/) - HL7 FHIR standards
- [PostgreSQL Docs](https://www.postgresql.org/docs/)

## Roadmap for Commercial Success

### Focus Areas (Phase 1)
1. ✅ System Stability
2. ✅ Fast Workflows
3. ✅ Easy-to-use UI
4. ✅ Strong Claims Processing
5. ✅ Reliable Document Upload

### Competitive Advantages
- Open-source, customizable
- Designed for small hospitals
- Regional language support (roadmap)
- Affordable infrastructure
- Fast implementation

## License

Apache License 2.0 - See LICENSE file

## Contact

For inquiries, support, or collaboration:
- Email: info@healthcareplatform.io
- Website: https://healthcareplatform.io

---

**Last Updated:** June 2026  
**Maintained By:** Healthcare Platform Team
