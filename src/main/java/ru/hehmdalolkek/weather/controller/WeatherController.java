package ru.hehmdalolkek.weather.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.hehmdalolkek.weather.service.TemperatureService;

import java.time.LocalDate;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final TemperatureService temperatureService;

    @GetMapping("/temperature")
    public ResponseEntity<?> getTemperature(@RequestParam("countryCode") String countryCode,
                                            @RequestParam("city") String city,
                                            @RequestParam(name = "date", required = false) LocalDate date) {
        if (Objects.isNull(date)) {
            return ResponseEntity
                    .ok(this.temperatureService.getLastTemperatureByCountryCodeAndCity(countryCode, city));
        } else {
            return ResponseEntity
                    .ok(this.temperatureService.getAllTemperaturesByCountryCodeAndCityAndDate(countryCode, city, date));
        }
    }

}
