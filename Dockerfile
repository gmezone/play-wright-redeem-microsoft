FROM maven:3-openjdk-18-slim AS build-env
#FROM maven:3-j

# Set the working directory to /app
WORKDIR /app
# Copy the pom.xml file to download dependencies
COPY pom.xml ./
# Copy local code to the container image.
COPY src ./src
#COPY keystore.p12 ./
# Download dependencies and build a release artifact.
RUN mvn package -DskipTests

# Use OpenJDK for base image.
# https://hub.docker.com/_/openjdk
# https://docs.docker.com/develop/develop-images/multistage-build/#use-multi-stage-builds
#FROM openjdk:11.0.16-jre-slim
#FROM openjdk:18-ea-1-jdk-slim
FROM mcr.microsoft.com/playwright/java:v1.39.0-jammy
# Copy the jar to the production image from the builder stage.
COPY --from=build-env /app/target/play-wright*.jar /play-wright.jar

EXPOSE 8080

# Run the web service on container startup.
CMD ["java", "-jar", "/play-wright.jar"]
