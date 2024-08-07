package ru.hehmdalolkek.weather.exception;

public class LocationException extends RuntimeException {

    public LocationException(String message) {
        super(message);
    }

    public static LocationException countryNotFound(String countryCode) {
        String message = "Country with code '%s' not found".formatted(countryCode);
        return new LocationException(message);
    }

    public static LocationException cityNotFound(String city) {
        String message = "City '%s' not found".formatted(city);
        return new LocationException(message);
    }

}
