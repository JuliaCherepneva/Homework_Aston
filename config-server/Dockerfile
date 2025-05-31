FROM openjdk:17-jdk-slim
LABEL authors="Shispon"
WORKDIR /app
COPY target/config-server-1.0-SNAPSHOT-exec.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]