# Healthcare Platform - Deployment Guide

## Table of Contents
1. [Local Development](#local-development)
2. [Docker Compose Deployment](#docker-compose-deployment)
3. [Production Deployment](#production-deployment)
4. [Monitoring & Maintenance](#monitoring--maintenance)

## Local Development

### Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL 15+
- Git

### Setup Steps

1. **Clone repository:**
   ```bash
   git clone <repo-url>
   cd healthcare-platform
   ```

2. **Start PostgreSQL:**
   ```bash
   docker run -d --name healthcare-postgres \
     -e POSTGRES_DB=healthcare_db \
     -e POSTGRES_USER=healthcare_user \
     -e POSTGRES_PASSWORD=healthcare_secure_password_123 \
     -p 5432:5432 \
     postgres:15-alpine
   ```

3. **Build all services:**
   ```bash
   mvn clean install -DskipTests
   ```

4. **Run individual services:**
   ```bash
   # Terminal 1 - Auth Service
   cd auth-service
   mvn spring-boot:run

   # Terminal 2 - Patient Service
   cd patient-service
   mvn spring-boot:run

   # Terminal 3 - Claims Service
   cd claims-service
   mvn spring-boot:run

   # Terminal 4 - Billing Service
   cd billing-service
   mvn spring-boot:run

   # Terminal 5 - File Service
   cd file-service
   mvn spring-boot:run

   # Terminal 6 - API Gateway
   cd api-gateway
   mvn spring-boot:run
   ```

## Docker Compose Deployment

### Quick Start

```bash
# Build all Docker images
docker-compose build

# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

### Service URLs

| Service | URL | Port |
|---------|-----|------|
| API Gateway | http://localhost:8080 | 8080 |
| Auth Service | http://localhost:8081 | 8081 |
| Patient Service | http://localhost:8082 | 8082 |
| Claims Service | http://localhost:8083 | 8083 |
| Billing Service | http://localhost:8084 | 8084 |
| File Service | http://localhost:8085 | 8085 |
| PostgreSQL | localhost:5432 | 5432 |

### Accessing Services

**Swagger UI:**
```
http://localhost:8080/swagger-ui.html
```

**Health Checks:**
```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8081/actuator/health
# ... for other services
```

## Production Deployment

### AWS Deployment

#### Using ECS (Elastic Container Service)

1. **Push images to ECR:**
   ```bash
   # Authenticate with ECR
   aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <account-id>.dkr.ecr.us-east-1.amazonaws.com

   # Tag and push images
   docker tag healthcare-platform/auth-service:latest <account-id>.dkr.ecr.us-east-1.amazonaws.com/healthcare-auth:latest
   docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/healthcare-auth:latest
   ```

2. **Create ECS Cluster:**
   ```bash
   aws ecs create-cluster --cluster-name healthcare-platform
   ```

3. **Register Task Definitions** for each service

4. **Create Services** in ECS cluster

#### Using RDS for PostgreSQL

1. **Create RDS Instance:**
   ```bash
   aws rds create-db-instance \
     --db-instance-identifier healthcare-db \
     --db-instance-class db.t3.micro \
     --engine postgres \
     --master-username healthcare_user \
     --master-user-password <secure-password> \
     --allocated-storage 20
   ```

2. **Initialize Database:**
   ```bash
   psql -h <rds-endpoint> -U healthcare_user -d healthcare_db -f docker/init-db.sql
   ```

### Kubernetes Deployment

1. **Create Namespace:**
   ```bash
   kubectl create namespace healthcare
   ```

2. **Create ConfigMap:**
   ```bash
   kubectl create configmap healthcare-config \
     --from-file=docker/init-db.sql \
     -n healthcare
   ```

3. **Create Secrets:**
   ```bash
   kubectl create secret generic healthcare-secrets \
     --from-literal=db-password=<password> \
     --from-literal=jwt-secret=<jwt-secret> \
     -n healthcare
   ```

4. **Deploy Services:**
   ```bash
   kubectl apply -f k8s/ -n healthcare
   ```

### SSL/TLS Configuration

1. **Using Let's Encrypt:**
   ```bash
   certbot certonly --standalone -d healthcareplatform.com
   ```

2. **Configure in Nginx:**
   ```nginx
   upstream api_gateway {
       server localhost:8080;
   }

   server {
       listen 443 ssl http2;
       server_name healthcareplatform.com;

       ssl_certificate /etc/letsencrypt/live/healthcareplatform.com/fullchain.pem;
       ssl_certificate_key /etc/letsencrypt/live/healthcareplatform.com/privkey.pem;

       location / {
           proxy_pass http://api_gateway;
           proxy_set_header X-Real-IP $remote_addr;
           proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
           proxy_set_header X-Forwarded-Proto $scheme;
       }
   }
   ```

## Monitoring & Maintenance

### Prometheus Setup

1. **Create prometheus.yml:**
   ```yaml
   global:
     scrape_interval: 15s

   scrape_configs:
     - job_name: 'auth-service'
       static_configs:
         - targets: ['localhost:8081']
       metrics_path: '/actuator/prometheus'

     # Add for other services...
   ```

2. **Start Prometheus:**
   ```bash
   docker run -d --name prometheus \
     -p 9090:9090 \
     -v $(pwd)/prometheus.yml:/etc/prometheus/prometheus.yml \
     prom/prometheus
   ```

### Grafana Setup

1. **Start Grafana:**
   ```bash
   docker run -d --name grafana \
     -p 3000:3000 \
     -e GF_SECURITY_ADMIN_PASSWORD=admin \
     grafana/grafana
   ```

2. **Add Prometheus datasource:**
   - URL: http://prometheus:9090

3. **Import dashboards for Spring Boot metrics**

### Backup & Recovery

1. **Database Backup:**
   ```bash
   docker exec healthcare-postgres pg_dump -U healthcare_user healthcare_db > backup.sql
   ```

2. **Database Restore:**
   ```bash
   docker exec -i healthcare-postgres psql -U healthcare_user healthcare_db < backup.sql
   ```

3. **File Storage Backup:**
   ```bash
   docker cp healthcare-file:/app/files ./files_backup
   ```

### Log Management

1. **Configure ELK Stack:**
   - Elasticsearch for storage
   - Logstash for processing
   - Kibana for visualization

2. **Application Logs:**
   ```bash
   docker-compose logs -f --tail=100
   ```

### Health Checks

Regular monitoring endpoints:
```bash
# All services
curl -s http://localhost:8080/actuator/health | jq .

# Individual services
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
curl http://localhost:8084/actuator/health
curl http://localhost:8085/actuator/health
```

### Database Maintenance

1. **Connection Pool Monitoring:**
   ```bash
   # Check HikariCP metrics
   curl http://localhost:8080/actuator/metrics/hikaricp.connections
   ```

2. **Index Analysis:**
   ```sql
   -- Check slow queries
   SELECT query, calls, total_time FROM pg_stat_statements 
   ORDER BY mean_time DESC LIMIT 10;
   ```

3. **Vacuum & Analyze:**
   ```sql
   VACUUM ANALYZE;
   ```

## Troubleshooting

### Common Issues

1. **Services won't start:**
   ```bash
   # Check logs
   docker-compose logs auth-service
   
   # Check database connection
   docker-compose exec postgres psql -U healthcare_user -d healthcare_db -c "SELECT 1"
   ```

2. **Port conflicts:**
   - Update ports in docker-compose.yml
   - Ensure ports are not in use: `lsof -i :8080`

3. **Database connection errors:**
   - Verify PostgreSQL is running
   - Check credentials in application.yml
   - Verify network connectivity

4. **Out of memory:**
   - Increase Docker memory limits
   - Review JVM heap settings in Dockerfile

## Performance Tuning

1. **Database Connection Pool:**
   ```yaml
   spring:
     datasource:
       hikari:
         maximum-pool-size: 20
         minimum-idle: 5
         connection-timeout: 30000
   ```

2. **JVM Tuning:**
   ```dockerfile
   ENTRYPOINT ["java", "-Xms512m", "-Xmx1024m", "-XX:+UseG1GC", "-jar", "app.jar"]
   ```

3. **API Gateway Caching:**
   - Implement Spring Cache for frequently accessed data
   - Use Redis for distributed caching

---

For more details, refer to [README.md](../README.md)
