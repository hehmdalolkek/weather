package ru.hehmdalolkek.weather.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hehmdalolkek.weather.dao.TemperatureDao;
import ru.hehmdalolkek.weather.dto.CurrentTemperatureResponse;
import ru.hehmdalolkek.weather.dto.DailyTemperatureResponse;
import ru.hehmdalolkek.weather.dto.LocationResponse;
import ru.hehmdalolkek.weather.dto.TemperatureResponse;
import ru.hehmdalolkek.weather.exception.LocationException;
import ru.hehmdalolkek.weather.exception.TemperatureException;
import ru.hehmdalolkek.weather.mapper.TemperatureMapper;
import ru.hehmdalolkek.weather.model.Temperature;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TemperatureServiceImplTest {

    @InjectMocks
    TemperatureServiceImpl temperatureService;

    @Mock
    CityService cityService;

    @Mock
    CountryService countryService;

    @Mock
    TemperatureDao temperatureDao;

    @Mock
    TemperatureMapper temperatureMapper;

    @Test
    @DisplayName("Test getLastTemperatureByCountryCodeAndCity() functionality " +
            "with an existing temperature, country and city")
    void givenCountryAndCity_whenGetLastTemperatureByCountryCodeAndCity_thenReturnTemperature() {
        // given
        String countryCode = "RU";
        String city = "Moscow";
        float temp = 22.5F;
        LocalDateTime dateTime = LocalDateTime.of(2024, Month.AUGUST, 6, 12, 0);
        Temperature temperature = new Temperature();
        CurrentTemperatureResponse current = CurrentTemperatureResponse.builder()
                .location(LocationResponse.builder()
                        .country(countryCode)
                        .city(city)
                        .build())
                .current(TemperatureResponse.builder()
                        .datetime(dateTime)
                        .temperature(temp)
                        .build())
                .build();
        when(countryService.countryExistsByCountryCode(anyString())).thenReturn(true);
        when(cityService.cityExistsInCountry(anyString(), anyString())).thenReturn(true);
        when(temperatureDao.findLastByCityNameAndCityCountryCode(anyString(), anyString()))
                .thenReturn(Optional.of(temperature));
        when(temperatureMapper.toCurrentTemperatureResponse(any(Temperature.class)))
                .thenReturn(current);

        // when
        CurrentTemperatureResponse result =
                temperatureService.getLastTemperatureByCountryCodeAndCity(countryCode, city);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(current);
        verify(countryService).countryExistsByCountryCode(anyString());
        verify(cityService).cityExistsInCountry(anyString(), anyString());
        verify(temperatureDao).findLastByCityNameAndCityCountryCode(anyString(), anyString());
        verify(temperatureMapper).toCurrentTemperatureResponse(any(Temperature.class));
        verifyNoMoreInteractions(countryService, cityService, temperatureDao, temperatureMapper);
    }

    @Test
    @DisplayName("Test getLastTemperatureByCountryCodeAndCity() functionality " +
            "with an existing country, city and non-existing temperature")
    void givenCountryAndCity_whenGetLastTemperatureByCountryCodeAndCity_thenThrowsException() {
        // given
        String countryCode = "RU";
        String city = "Moscow";
        when(countryService.countryExistsByCountryCode(anyString())).thenReturn(true);
        when(cityService.cityExistsInCountry(anyString(), anyString())).thenReturn(true);
        when(temperatureDao.findLastByCityNameAndCityCountryCode(anyString(), anyString()))
                .thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() ->
                temperatureService.getLastTemperatureByCountryCodeAndCity(countryCode, city))
                .isInstanceOf(TemperatureException.class)
                .hasMessage("Temperature for RU/Moscow not found");

        verify(countryService).countryExistsByCountryCode(anyString());
        verify(cityService).cityExistsInCountry(anyString(), anyString());
        verify(temperatureDao).findLastByCityNameAndCityCountryCode(anyString(), anyString());
        verifyNoMoreInteractions(countryService, cityService, temperatureDao, temperatureMapper);
    }

    @Test
    @DisplayName("Test getLastTemperatureByCountryCodeAndCity() functionality " +
            "with an existing country and non-existing city")
    void givenCountryAndNonExistsCity_whenGetLastTemperatureByCountryCodeAndCity_thenThrowsException() {
        // given
        String countryCode = "RU";
        String city = "Berlin";
        when(countryService.countryExistsByCountryCode(anyString())).thenReturn(true);
        when(cityService.cityExistsInCountry(anyString(), anyString())).thenReturn(false);

        // when
        // then
        assertThatThrownBy(() ->
                temperatureService.getLastTemperatureByCountryCodeAndCity(countryCode, city))
                .isInstanceOf(LocationException.class)
                .hasMessage("City 'Berlin' not found");

        verify(countryService).countryExistsByCountryCode(anyString());
        verify(cityService).cityExistsInCountry(anyString(), anyString());
        verifyNoMoreInteractions(countryService, cityService, temperatureDao, temperatureMapper);
    }

    @Test
    @DisplayName("Test getLastTemperatureByCountryCodeAndCity() functionality " +
            "with an existing city and non-existing country")
    void givenCityAndNonExistsCountry_whenGetLastTemperatureByCountryCodeAndCity_thenThrowsException() {
        // given
        String countryCode = "WW";
        String city = "Moscow";
        when(countryService.countryExistsByCountryCode(anyString())).thenReturn(false);

        // when
        // then
        assertThatThrownBy(() ->
                temperatureService.getLastTemperatureByCountryCodeAndCity(countryCode, city))
                .isInstanceOf(LocationException.class)
                .hasMessage("Country with code 'WW' not found");

        verify(countryService).countryExistsByCountryCode(anyString());
        verifyNoMoreInteractions(countryService, cityService, temperatureDao, temperatureMapper);
    }

    @Test
    @DisplayName("Test getAllTemperaturesByCountryCodeAndCityAndDate() functionality " +
            "with an existing temperature, country and city")
    void givenCountryCityDate_whenGetAllTemperaturesByCountryCodeAndCityAndDate_thenReturnTemperatureList() {
        // given
        String countryCode = "RU";
        String city = "Moscow";
        float temp = 24.4F;
        LocalDate date = LocalDate.of(2024, Month.AUGUST, 6);
        LocalDateTime dateTime = LocalDateTime.of(2024, Month.AUGUST, 6, 12, 0);
        Temperature temperature = new Temperature();
        temperature.setTemperature(temp);
        List<Temperature> temperatureList = List.of(temperature);
        DailyTemperatureResponse daily = DailyTemperatureResponse.builder()
                .location(LocationResponse.builder()
                        .city(city)
                        .country(countryCode)
                        .build())
                .daily(List.of(TemperatureResponse.builder()
                                .temperature(temp)
                                .datetime(dateTime)
                        .build()))
                .build();
        when(countryService.countryExistsByCountryCode(anyString())).thenReturn(true);
        when(cityService.cityExistsInCountry(anyString(), anyString())).thenReturn(true);
        when(temperatureDao.findAllByDateTimeAndCityNameAndCityCountryCode(
                anyString(),
                anyString(),
                any(LocalDate.class)))
                .thenReturn(temperatureList);
        when(temperatureMapper.toDailyTemperatureResponse(anyList())).thenReturn(daily);

        // when
        DailyTemperatureResponse result =
                temperatureService.getAllTemperaturesByCountryCodeAndCityAndDate(countryCode, city, date);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(daily);
        verify(countryService).countryExistsByCountryCode(anyString());
        verify(cityService).cityExistsInCountry(anyString(), anyString());
        verify(temperatureDao).findAllByDateTimeAndCityNameAndCityCountryCode(
                anyString(),
                anyString(),
                any(LocalDate.class));
        verify(temperatureMapper).toDailyTemperatureResponse(anyList());
        verifyNoMoreInteractions(countryService, cityService, temperatureDao, temperatureMapper);
    }

    @Test
    @DisplayName("Test getAllTemperaturesByCountryCodeAndCityAndDate() functionality " +
            "with an existing country, city and non-existing temperature")
    void givenCountryCityDate_whenGetAllTemperaturesByCountryCodeAndCityAndDate_thenThrowsException() {
        // given
        String countryCode = "RU";
        String city = "Moscow";
        LocalDate date = LocalDate.of(2024, Month.AUGUST, 6);
        when(countryService.countryExistsByCountryCode(anyString())).thenReturn(true);
        when(cityService.cityExistsInCountry(anyString(), anyString())).thenReturn(true);
        when(temperatureDao.findAllByDateTimeAndCityNameAndCityCountryCode(
                anyString(),
                anyString(),
                any(LocalDate.class)))
                .thenReturn(new ArrayList<>());

        // when
        // then
        assertThatThrownBy(() ->
                temperatureService.getAllTemperaturesByCountryCodeAndCityAndDate(countryCode, city, date))
                .isInstanceOf(TemperatureException.class)
                .hasMessage("Temperature for RU/Moscow/2024-08-06 not found");

        verify(countryService).countryExistsByCountryCode(anyString());
        verify(cityService).cityExistsInCountry(anyString(), anyString());
        verify(temperatureDao).findAllByDateTimeAndCityNameAndCityCountryCode(
                anyString(),
                anyString(),
                any(LocalDate.class));
        verifyNoMoreInteractions(countryService, cityService, temperatureDao, temperatureMapper);
    }

    @Test
    @DisplayName("Test getAllTemperaturesByCountryCodeAndCityAndDate() functionality " +
            "with an existing country and non-existing city")
    void givenCountryAndNonExistsCity_whenGetAllTemperaturesByCountryCodeAndCityAndDate_thenThrowsException() {
        // given
        String countryCode = "RU";
        String city = "Berlin";
        LocalDate date = LocalDate.of(2024, Month.AUGUST, 6);
        when(countryService.countryExistsByCountryCode(anyString())).thenReturn(true);
        when(cityService.cityExistsInCountry(anyString(), anyString())).thenReturn(false);

        // when
        // then
        assertThatThrownBy(() ->
                temperatureService.getAllTemperaturesByCountryCodeAndCityAndDate(countryCode, city, date))
                .isInstanceOf(LocationException.class)
                .hasMessage("City 'Berlin' not found");

        verify(countryService).countryExistsByCountryCode(anyString());
        verify(cityService).cityExistsInCountry(anyString(), anyString());
        verifyNoMoreInteractions(countryService, cityService, temperatureDao, temperatureMapper);
    }

    @Test
    @DisplayName("Test getAllTemperaturesByCountryCodeAndCityAndDate() functionality " +
            "with an existing city and non-existing country")
    void givenCityAndNonExistsCountry_whenGetAllTemperaturesByCountryCodeAndCityAndDate_thenThrowsException() {
        // given
        String countryCode = "WW";
        String city = "Moscow";
        LocalDate date = LocalDate.of(2024, Month.AUGUST, 6);
        when(countryService.countryExistsByCountryCode(anyString())).thenReturn(false);

        // when
        // then
        assertThatThrownBy(() ->
                temperatureService.getAllTemperaturesByCountryCodeAndCityAndDate(countryCode, city, date))
                .isInstanceOf(LocationException.class)
                .hasMessage("Country with code 'WW' not found");

        verify(countryService).countryExistsByCountryCode(anyString());
        verifyNoMoreInteractions(countryService, cityService, temperatureDao, temperatureMapper);
    }

}