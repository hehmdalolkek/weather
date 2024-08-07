package ru.hehmdalolkek.weather.exception;

import java.time.LocalDate;

public class TemperatureException extends RuntimeException {

    public TemperatureException(String message) {
        super(message);
    }

    public static TemperatureException temperaturesNotFound(String city, String countryCode, LocalDate date) {
        String message = "Temperature for %s/%s/%s not found".formatted(countryCode, city, date);
        return new TemperatureException(message);
    }

    public static TemperatureException temperatureNotFound(String city, String countryCode) {
        String message = "Temperature for %s/%s not found".formatted(countryCode, city);
        return new TemperatureException(message);
    }

}
