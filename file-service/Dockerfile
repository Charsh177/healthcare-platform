FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/file-service-1.0.0.jar app.jar

EXPOSE 8085

HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8085/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
