package ru.hehmdalolkek.weather.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.hehmdalolkek.weather.exception.LocationException;
import ru.hehmdalolkek.weather.exception.TemperatureException;

@ControllerAdvice
public class WeatherControllerAdvice {

    @ExceptionHandler(TemperatureException.class)
    public ResponseEntity<ProblemDetail> handleTemperatureException(TemperatureException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, exception.getMessage()
        );
        return ResponseEntity.of(problemDetail)
                .build();
    }

    @ExceptionHandler(LocationException.class)
    public ResponseEntity<ProblemDetail> handleLocationException(LocationException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, exception.getMessage()
        );
        return ResponseEntity.of(problemDetail)
                .build();
    }

}
