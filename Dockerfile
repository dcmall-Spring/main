# (1) 빌드 단계
FROM gradle:7.6.0-jdk17 AS builder
WORKDIR /app
COPY . /app
RUN gradle clean build -x test

# (2) 런타임 단계
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar myapp.jar
EXPOSE 8080
LABEL authors="LJH"
ENTRYPOINT ["java", "-jar", "myapp.jar"]
