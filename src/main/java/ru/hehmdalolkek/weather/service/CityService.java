package ru.hehmdalolkek.weather.service;

import ru.hehmdalolkek.weather.model.City;

public interface CityService {

    boolean cityExistsInCountry(String city, String countryCode);

    City getCityByNameAndCountryCode(String city, String countryCode);

    void saveCity(City city);

}
