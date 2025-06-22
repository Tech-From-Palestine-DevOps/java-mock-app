# Stage 1: Build with Maven
FROM maven:3.8.6-eclipse-temurin-17-alpine AS builder
# create a folder in the container
WORKDIR /app
COPY pom.xml .
# Leverage Docker cache by downloading dependencies first
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Runtime image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built JAR from builder
COPY --from=builder /app/target/*.jar app.jar

# Security optimizations
RUN addgroup -S spring && adduser -S spring -G spring \
    && chown spring:spring /app \
    && chmod 755 /app

USER spring:spring

# Runtime optimizations
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar"]
