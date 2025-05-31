FROM openjdk:17-jdk-slim
LABEL authors="Shispon"
WORKDIR /app
COPY target/api-gateway-1.0-SNAPSHOT-exec.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]