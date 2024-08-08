package ru.hehmdalolkek.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hehmdalolkek.weather.dao.TemperatureDao;
import ru.hehmdalolkek.weather.dto.CurrentTemperatureResponse;
import ru.hehmdalolkek.weather.dto.DailyTemperatureResponse;
import ru.hehmdalolkek.weather.exception.LocationException;
import ru.hehmdalolkek.weather.exception.TemperatureException;
import ru.hehmdalolkek.weather.mapper.TemperatureMapper;
import ru.hehmdalolkek.weather.model.Temperature;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TemperatureServiceImpl implements TemperatureService {

    private final TemperatureDao temperatureDao;

    private final CountryService countryService;

    private final CityService cityService;

    private final TemperatureMapper temperatureMapper;

    @Override
    public DailyTemperatureResponse getAllTemperaturesByCountryCodeAndCityAndDate(String countryCode,
                                                                                  String city,
                                                                                  LocalDate date) throws LocationException, TemperatureException {
        this.checkExistsCityCountry(city, countryCode);
        List<Temperature> temperatureList =
                this.temperatureDao.findAllByDateTimeAndCityNameAndCityCountryCode(city, countryCode, date);
        if (temperatureList.isEmpty()) {
            throw TemperatureException.temperaturesNotFound(city, countryCode, date);
        }
        return this.temperatureMapper.toDailyTemperatureResponse(temperatureList);
    }

    @Override
    public CurrentTemperatureResponse getLastTemperatureByCountryCodeAndCity(String countryCode,
                                                                             String city) throws LocationException, TemperatureException {
        this.checkExistsCityCountry(city, countryCode);
        Temperature temperature = this.temperatureDao.findLastByCityNameAndCityCountryCode(city, countryCode)
                .orElseThrow(() -> TemperatureException.temperatureNotFound(city, countryCode));
        return this.temperatureMapper.toCurrentTemperatureResponse(temperature);
    }

    @Override
    public void saveAllTemperatures(List<Temperature> temperatureList) {
        this.temperatureDao.saveAll(temperatureList);
    }

    private void checkExistsCityCountry(String city, String countryCode) throws LocationException {
        boolean countryExists = this.countryService.countryExistsByCountryCode(countryCode);
        if (!countryExists) {
            throw LocationException.countryNotFound(countryCode);
        }
        boolean cityExists = this.cityService.cityExistsInCountry(city, countryCode);
        if (!cityExists) {
            throw LocationException.cityNotFound(city);
        }
    }

}
