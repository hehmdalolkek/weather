package ru.hehmdalolkek.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hehmdalolkek.weather.dao.CountryDao;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryDao countryDao;

    @Override
    public boolean countryExistsByCountryCode(String countryCode) {
        return this.countryDao.existsByCodeIgnoreCase(countryCode);
    }

}
