package ru.hehmdalolkek.weather.service;

import ru.hehmdalolkek.weather.dto.CurrentTemperatureResponse;
import ru.hehmdalolkek.weather.dto.DailyTemperatureResponse;
import ru.hehmdalolkek.weather.exception.LocationException;
import ru.hehmdalolkek.weather.exception.TemperatureException;
import ru.hehmdalolkek.weather.model.Temperature;

import java.time.LocalDate;
import java.util.List;

public interface TemperatureService {

    DailyTemperatureResponse getAllTemperaturesByCountryCodeAndCityAndDate(
            String countryCode,
            String city,
            LocalDate date
    ) throws LocationException, TemperatureException;

    CurrentTemperatureResponse getLastTemperatureByCountryCodeAndCity(
            String countryCode,
            String city
    ) throws LocationException, TemperatureException;

    void saveAllTemperatures(List<Temperature> temperatureList);

}
