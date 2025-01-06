#
# Build stage
#
FROM maven:3-openjdk-17 AS build
WORKDIR /app
COPY . /app/
# Add -DskipTests to skip tests during build (if necessary)
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copy the generated JAR from the build stage
COPY --from=build /app/target/*.jar /app/backend-byspring.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/backend-byspring.jar"]



# #
# # Build stage
# #
# FROM maven:3-openjdk-21 AS build
# WORKDIR /app
# COPY . /app/
# RUN mvn clean package
#
# #
# # Package stage
# #
# FROM openjdk:21-jdk-slim
# WORKDIR /app
# COPY target/*.jar /app/backend-byspring.jar
# EXPOSE 8080
# ENTRYPOINT ["java", "-jar", "backend-byspring.jar"]
#

# CMD
# docker build -t backend-byspring.jar .
# docker tag backend-byspring.jar chhomdavit/dev:backend-byspring.jar
# docker push chhomdavit/dev:backend-byspring.jar
