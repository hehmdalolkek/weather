package ru.hehmdalolkek.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hehmdalolkek.weather.dao.CountryDao;
import ru.hehmdalolkek.weather.model.Country;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryDao countryDao;

    @Override
    public boolean countryExistsByCountryCode(String countryCode) {
        return this.countryDao.existsByCodeIgnoreCase(countryCode);
    }

    @Override
    public Country getCountryByCode(String countryCode) {
        return this.countryDao.findByCode(countryCode);
    }

    @Override
    public void saveCountry(Country country) {
        this.countryDao.save(country);
    }

}
