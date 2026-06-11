# Healthcare Platform - Development Guide

## Architecture Overview

```
┌─────────────────────────┐
│   React Frontend (Optional) │
└────────────┬────────────┘
             │
┌────────────▼────────────┐
│    API Gateway (8080)   │
└────────────┬────────────┘
             │
    ┌────────┼────────┬─────────┬──────────┐
    │        │        │         │          │
┌───▼──┐ ┌──▼──┐ ┌──▼───┐ ┌──▼───┐ ┌───▼──┐
│Auth  │ │Patient  │ │Claims │ │Billing │ │File  │
│(8081)│ │(8082)   │ │(8083) │ │(8084)  │ │(8085)│
└─┬──┬─┘ └──┬──┬─┘ └──┬───┘ └──┬───┘ └───┬──┘
  │  │      │  │      │        │         │
  └──┴──────┴──┴──────┴────────┴─────────┘
             │
     ┌───────▼────────┐
     │  PostgreSQL    │
     │  (localhost:   │
     │   5432)        │
     └────────────────┘
```

## Project Structure

```
healthcare-platform/
├── auth-service/                    # User authentication
│   ├── src/main/java/.../auth/
│   │   ├── controller/              # REST endpoints
│   │   ├── service/                 # Business logic
│   │   ├── entity/                  # JPA entities
│   │   ├── dto/                     # Data transfer objects
│   │   ├── repository/              # Database access
│   │   ├── security/                # JWT & security
│   │   └── config/                  # Spring configuration
│   └── src/main/resources/
│       └── application.yml          # Service configuration
│
├── patient-service/                 # Patient management
├── claims-service/                  # Claims processing
├── billing-service/                 # Billing operations
├── file-service/                    # File upload/storage
│
├── api-gateway/                     # Request routing
├── common-lib/                      # Shared utilities
│
├── frontend/                        # React UI (optional)
├── docker/                          # Docker configs
├── scripts/                         # Deployment scripts
├── docs/                            # Documentation
│
├── pom.xml                          # Root Maven POM
└── docker-compose.yml               # Service orchestration
```

## Setting Up Development Environment

### Prerequisites Installation

1. **Install Java 17:**
   ```bash
   # macOS
   brew install openjdk@17

   # Linux (Ubuntu/Debian)
   sudo apt-get install openjdk-17-jdk

   # Windows
   # Download from https://adoptium.net/
   ```

2. **Install Maven:**
   ```bash
   # macOS
   brew install maven

   # Linux
   sudo apt-get install maven

   # Windows
   # Download from https://maven.apache.org/download.cgi
   ```

3. **Install Docker:**
   - https://docs.docker.com/get-docker/

4. **Install Git:**
   - https://git-scm.com/

### IDE Setup (IntelliJ IDEA or VS Code)

**IntelliJ IDEA:**
1. Open project folder
2. Configure SDK: File → Project Structure → Project → Set JDK 17
3. Enable Annotation Processing: File → Settings → Build → Compiler → Annotation Processors → Enable
4. Install plugins: Lombok, Spring Boot, Docker

**VS Code:**
1. Install extensions:
   - Extension Pack for Java
   - Spring Boot Extension Pack
   - REST Client
   - Docker

### Database Setup

1. **Start PostgreSQL (Docker):**
   ```bash
   docker run -d \
     --name healthcare-postgres \
     -e POSTGRES_DB=healthcare_db \
     -e POSTGRES_USER=healthcare_user \
     -e POSTGRES_PASSWORD=healthcare_secure_password_123 \
     -p 5432:5432 \
     postgres:15-alpine
   ```

2. **Initialize Database:**
   ```bash
   docker exec -i healthcare-postgres psql -U healthcare_user -d healthcare_db < docker/init-db.sql
   ```

## Development Workflow

### 1. Creating a New Feature

1. **Create feature branch:**
   ```bash
   git checkout -b feature/patient-search
   ```

2. **Implement feature:**
   - Add entity in `entity/` package
   - Add DTO in `dto/` package
   - Add repository interface in `repository/`
   - Add service logic in `service/`
   - Add REST endpoint in `controller/`

3. **Example: Patient Search**

   **Entity** (PatientSearch.java):
   ```java
   @Repository
   public interface PatientRepository extends JpaRepository<Patient, UUID> {
       List<Patient> findByFirstNameContainingIgnoreCase(String firstName);
   }
   ```

   **Service** (PatientService.java):
   ```java
   public List<PatientResponse> search(String name) {
       return patientRepository.findByFirstNameContainingIgnoreCase(name)
           .stream()
           .map(this::mapToResponse)
           .collect(Collectors.toList());
   }
   ```

   **Controller** (PatientController.java):
   ```java
   @GetMapping("/search")
   public ResponseEntity<List<PatientResponse>> search(@RequestParam String name) {
       return ResponseEntity.ok(patientService.search(name));
   }
   ```

### 2. Running Tests

```bash
# Run all tests
mvn test

# Run specific service tests
mvn test -pl patient-service

# Run with coverage
mvn test jacoco:report
```

### 3. Code Quality Checks

```bash
# Check code style
mvn checkstyle:check

# Run SonarQube analysis
mvn sonar:sonar -Dsonar.host.url=http://localhost:9000
```

## Coding Standards

### Java Naming Conventions

```java
// Classes - PascalCase
public class PatientService { }

// Interfaces - PascalCase
public interface PatientRepository { }

// Methods & Variables - camelCase
private String patientName;
public void registerPatient() { }

// Constants - UPPER_CASE
private static final String STATUS_ACTIVE = "ACTIVE";

// Package names - lowercase
com.healthcare.patient.service
```

### REST API Naming

```
GET    /api/v1/patients              - Get all patients
POST   /api/v1/patients              - Create patient
GET    /api/v1/patients/{id}         - Get patient by ID
PUT    /api/v1/patients/{id}         - Update patient
DELETE /api/v1/patients/{id}         - Delete patient
GET    /api/v1/patients/search       - Search patients
```

### Response Format

```json
{
  "success": true,
  "data": {
    "patientId": "uuid",
    "firstName": "John"
  },
  "message": "Patient created successfully",
  "error": null
}
```

### Error Response

```json
{
  "success": false,
  "data": null,
  "message": null,
  "error": "Validation failed: Invalid email format"
}
```

## Debugging

### Debug Mode

1. **IntelliJ IDEA:**
   - Right-click service → Run → Run with Debug

2. **Command Line:**
   ```bash
   export JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
   mvn spring-boot:run
   ```

### Viewing Logs

```bash
# Real-time logs
docker-compose logs -f patient-service

# Last 100 lines
docker-compose logs --tail=100 patient-service

# Filter logs
docker-compose logs auth-service | grep ERROR
```

### Database Debugging

```bash
# Connect to PostgreSQL
docker exec -it healthcare-postgres psql -U healthcare_user -d healthcare_db

# List tables
\dt auth_schema.*

# Query data
SELECT * FROM auth_schema.users;

# Check indexes
\di
```

## Adding a New Service

### Step 1: Create Service Structure

```bash
mkdir -p new-service/src/main/java/com/healthcare/newservice/{controller,service,entity,dto,repository,config}
mkdir -p new-service/src/main/resources
mkdir -p new-service/src/test/java/com/healthcare/newservice
```

### Step 2: Create pom.xml

```xml
<parent>
    <groupId>com.healthcare</groupId>
    <artifactId>healthcare-platform</artifactId>
    <version>1.0.0</version>
</parent>

<artifactId>new-service</artifactId>
<name>New Service</name>
```

### Step 3: Add to Root pom.xml

```xml
<modules>
    <!-- existing modules -->
    <module>new-service</module>
</modules>
```

### Step 4: Create Dockerfile

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/new-service-1.0.0.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Step 5: Update docker-compose.yml

```yaml
new-service:
  build:
    context: ./new-service
    dockerfile: Dockerfile
  container_name: healthcare-new
  environment:
    SERVER_PORT: 8086
    SPRING_PROFILES_ACTIVE: docker
  depends_on:
    postgres:
      condition: service_healthy
  networks:
    - healthcare-network
```

## Database Migrations

### Using Flyway

1. **Create migration file** (`db/migration/V1__Initial_schema.sql`)
2. **Run migrations automatically** on startup

Configuration in `application.yml`:
```yaml
spring:
  flyway:
    locations: classpath:db/migration
    baseline-on-migrate: true
```

## Performance Optimization

### Database Query Optimization

```java
// Use @Query for complex queries
@Query("SELECT p FROM Patient p WHERE p.uhid = :uhid")
Optional<Patient> findByUhid(@Param("uhid") String uhid);

// Use projections for subset of fields
@Query("SELECT new com.healthcare.patient.dto.PatientResponse(...) FROM Patient p")
List<PatientResponse> findAll();

// Use pagination
Page<Patient> findAll(Pageable pageable);
```

### Caching

```java
@Service
public class PatientService {
    @Cacheable(value = "patients", key = "#id")
    public PatientResponse getPatientById(UUID id) {
        // Cache for 1 hour
    }
    
    @CacheEvict(value = "patients", key = "#id")
    public void updatePatient(UUID id, PatientRequest request) {
        // Invalidate cache
    }
}
```

## Testing

### Unit Testing

```java
@SpringBootTest
class PatientServiceTest {
    @MockBean
    private PatientRepository patientRepository;
    
    @InjectMocks
    private PatientService patientService;
    
    @Test
    void testCreatePatient() {
        // Test logic
    }
}
```

### Integration Testing

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PatientControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void testGetPatient() {
        // Test logic
    }
}
```

## Deployment Checklist

- [ ] All tests passing
- [ ] Code reviewed
- [ ] No security warnings
- [ ] Database migrations applied
- [ ] Documentation updated
- [ ] Environment variables configured
- [ ] SSL certificates valid
- [ ] Backups configured
- [ ] Monitoring enabled
- [ ] Logs centralized

---

For more details, see [README.md](../README.md) and [DEPLOYMENT.md](DEPLOYMENT.md)
