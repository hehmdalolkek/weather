package ru.hehmdalolkek.weather.service;

import ru.hehmdalolkek.weather.model.Country;

public interface CountryService {

    boolean countryExistsByCountryCode(String countryCode);

    Country getCountryByCode(String countryCode);

    void saveCountry(Country country);

}
