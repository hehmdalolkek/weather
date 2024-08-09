FROM maven:3.9.8-eclipse-temurin-21-alpine as build
WORKDIR /build
COPY pom.xml .
COPY src src
RUN mvn clean install -DskipTests

FROM eclipse-temurin:21-alpine
COPY --from=build /build/target/weather-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]