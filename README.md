# Weather
REST service for storing and providing information about temperature in cities.\
Temperature for cities is requested from three publicly available REST weather services. The values received from services for the same city at the current moment are averaged and added to the DB.\
The service provides a REST endpoint through which, by specifying the city/country and date, you can get all the temperature values ​​available in the DB.\
Settings: specifies the list of cities; specifies the frequency of polling services.

## Technologies
* Java 21
* Maven
* Spring Boot 3
* Spring Web
* Spring Data JPA
* Lombok
* MapStruct
* JUnit
* Mockito
* PostgreSQL
* LiquiBase
* Testcontainers

## Run with docker
1. First, you need to create and fill out .env file in the project root according to the example:
```
# DB
DB_URL=jdbc:postgresql://db:5432/weather
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_DB=weather

# APP
WEATHERBIT_API_KEY={Your API key}
OPENWEATHERMAP_API_KEY={Your API key}
WEATHERAPI_API_KEY={Your API key}
# APP optional
WEATHER_REQUEST_RATE_TIME=15
WEATHER_REQUEST_LOCATIONS=RU/Moscow, RU/Kazan, RU/Samara
WEATHERBIT_BASE_URL=https://api.weatherbit.io/v2.0
OPENWEATHERMAP_BASE_URL=https://api.openweathermap.org/data/2.5
WEATHERAPI_BASE_URL=https://api.weatherapi.com/v1
```
2. Next, you can run the application and database using docker-compose.yaml:
```
docker compose up -d
```

## Run locally
1. First, you need to start the PostgreSQL database.
2. Next, you need to create and fill out the .env file in the project root with data about the database and application.
```
# DB
DB_URL=jdbc:postgresql://localhost:5432/weather
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_DB=weather

# APP
WEATHERBIT_API_KEY={Your API key}
OPENWEATHERMAP_API_KEY={Your API key}
WEATHERAPI_API_KEY={Your API key}
# APP optional
WEATHER_REQUEST_RATE_TIME=15
WEATHER_REQUEST_LOCATIONS=RU/Moscow, RU/Kazan, RU/Samara
WEATHERBIT_BASE_URL=https://api.weatherbit.io/v2.0
OPENWEATHERMAP_BASE_URL=https://api.openweathermap.org/data/2.5
WEATHERAPI_BASE_URL=https://api.weatherapi.com/v1
```
3. Finally, you can run the application locally:
```
./mvnw install -DskipTests
java -jar target/weather-0.0.1-SNAPSHOT.jar
```
