# Stage 1: Base - setup general eviroments 
FROM maven:3.9.6-eclipse-temurin-21-alpine AS base
WORKDIR /app

# Copy pom.xml and download dependencies to cache them
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and build the jar
# create a "stage" name Development 
FROM base AS development 
COPY src ./src

#Expose port ứng dụng và port debug 
EXPOSE 8080
EXPOSE 5005
# chạy trực tiếp bằng Maven để hỗ trợ Hot Reload và remote Debug 
CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005'"]

# Next stage: Build - đóng gói file Jar 
FROM base AS build-stage 
COPY src ./src 
RUN mvn clean package -DskipTests 

# Product - the lightest version to run on server 
# run docker build --target production -t smart-clinic-prod .

FROM eclipse-temurin:21-jre-alpine AS production 
WORKDIR /app
COPY --from=build-stage /app/target/*.jar app.jar 
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]