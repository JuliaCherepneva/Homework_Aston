FROM openjdk:17-jdk-slim
LABEL authors="Shispon"
WORKDIR /app
COPY target/notification-service-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]