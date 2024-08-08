package ru.hehmdalolkek.weather.service.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hehmdalolkek.weather.model.City;
import ru.hehmdalolkek.weather.model.Country;
import ru.hehmdalolkek.weather.model.Temperature;
import ru.hehmdalolkek.weather.service.CityService;
import ru.hehmdalolkek.weather.service.CountryService;
import ru.hehmdalolkek.weather.service.TemperatureService;
import ru.hehmdalolkek.weather.service.weatherapi.TemperatureRestClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TemperatureIntegrationService {

    private final TemperatureRestClient temperatureWeatherbitRestApiClient;

    private final TemperatureRestClient temperatureOpenweathermapRestApiClient;

    private final TemperatureRestClient temperatureWeatherapiRestApiClient;

    private final TemperatureService temperatureService;

    private final CountryService countryService;

    private final CityService cityService;

    private final List<String> locations;

    public TemperatureIntegrationService(@Qualifier("weatherbit") TemperatureRestClient temperatureWeatherbitRestApiClient,
                                         @Qualifier("openweathermap") TemperatureRestClient temperatureOpenweathermapRestApiClient,
                                         @Qualifier("weatherapi") TemperatureRestClient temperatureWeatherapiRestApiClient,
                                         TemperatureService temperatureService,
                                         CountryService countryService,
                                         CityService cityService,
                                         @Value("${weather.request.locations}") List<String> locations) {
        this.temperatureWeatherbitRestApiClient = temperatureWeatherbitRestApiClient;
        this.temperatureOpenweathermapRestApiClient = temperatureOpenweathermapRestApiClient;
        this.temperatureWeatherapiRestApiClient = temperatureWeatherapiRestApiClient;
        this.temperatureService = temperatureService;
        this.countryService = countryService;
        this.cityService = cityService;
        this.locations = locations;
    }

    @Transactional
    @Scheduled(
            fixedRateString = "${weather.request.rate.time:60}",
            timeUnit = TimeUnit.MINUTES,
            scheduler = "taskScheduler"
    )
    public void updateTemperatureDataFromApi() {
        log.info("Starts update temperature from API for locations: {}.", locations);

        List<Temperature> temperaturesToSave = new ArrayList<>();
        this.locations.forEach(location -> {
            String[] locationSplit = location.split("/");
            String countryCode = locationSplit[0];
            String cityName = locationSplit[1];

            double weatherbitCurrentTemperature = 0;
            double openweathermapCurrentTemperature = 0;
            double weatherapiCurrentTemperature = 0;
            try {
                weatherbitCurrentTemperature =
                        this.temperatureWeatherbitRestApiClient.getCurrentTemperature(countryCode, cityName);
                openweathermapCurrentTemperature =
                        this.temperatureOpenweathermapRestApiClient.getCurrentTemperature(countryCode, cityName);
                weatherapiCurrentTemperature =
                        this.temperatureWeatherapiRestApiClient.getCurrentTemperature(countryCode, cityName);
            } catch (Exception e) {
                log.warn("Temperature update error from API. Location = {}.\n{}", location, e.getMessage());
                return;
            }
            double avgTemperature = this.averageTemperature(weatherbitCurrentTemperature,
                    openweathermapCurrentTemperature, weatherapiCurrentTemperature);

            Country country = this.countryService.getCountryByCode(countryCode);
            if (Objects.isNull(country)) {
                country = new Country();
                country.setCode(countryCode);
                this.countryService.saveCountry(country);
            }

            City city = this.cityService.getCityByNameAndCountryCode(cityName, countryCode);
            if (Objects.isNull(city)) {
                city = new City();
                city.setName(cityName);
                country.addCity(city);
                this.cityService.saveCity(city);
            }

            Temperature temperature = new Temperature();
            temperature.setTemperature(avgTemperature);
            temperature.setCity(city);
            temperature.setDateTime(LocalDateTime.now());
            temperaturesToSave.add(temperature);
        });

        this.temperatureService.saveAllTemperatures(temperaturesToSave);
        log.info("Completed updating temperature from API.");
    }

    private double averageTemperature(double... temperatures) {
        double avgTemperature = Arrays.stream(temperatures).sum() / temperatures.length;
        return (double) Math.round(avgTemperature * 10) / 10;
    }

}
