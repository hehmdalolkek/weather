package ru.hehmdalolkek.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hehmdalolkek.weather.dao.CityDao;
import ru.hehmdalolkek.weather.model.City;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityDao cityDao;

    @Override
    public boolean cityExistsInCountry(String city, String countryCode) {
        return this.cityDao.existsByNameAndCountryCode(city, countryCode);
    }

    @Override
    public City getCityByNameAndCountryCode(String city, String countryCode) {
        return this.cityDao.findByNameAndCountryCode(city, countryCode);
    }

    @Override
    public void saveCity(City city) {
        this.cityDao.save(city);
    }

}
