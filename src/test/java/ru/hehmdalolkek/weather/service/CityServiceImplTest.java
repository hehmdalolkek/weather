package ru.hehmdalolkek.weather.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hehmdalolkek.weather.dao.CityDao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceImplTest {

    @InjectMocks
    CityServiceImpl cityService;

    @Mock
    CityDao cityDao;

    @Test
    @DisplayName("Test cityExistsInCountry() functionality with an exists city in country")
    void givenCityName_whenCityExistsInCountry_thenReturnTrue() {
        // given
        String countryCode = "RU";
        String cityName = "Samara";
        when(cityDao.existsByNameAndCountryCode(anyString(), anyString()))
                .thenReturn(true);

        // when
        boolean result = cityService.cityExistsInCountry(cityName, countryCode);

        // then
        assertThat(result).isTrue();
        verify(cityDao).existsByNameAndCountryCode(anyString(), anyString());
        verifyNoMoreInteractions(cityDao);
    }

    @Test
    @DisplayName("Test cityExistsInCountry() functionality with non-exists city in country")
    void givenCityName_whenCityExistsInCountry_thenReturnFalse() {
        // given
        String countryCode = "AF";
        String cityName = "New York";
        when(cityDao.existsByNameAndCountryCode(anyString(), anyString()))
                .thenReturn(false);

        // when
        boolean result = cityService.cityExistsInCountry(cityName, countryCode);

        // then
        assertThat(result).isFalse();
        verify(cityDao).existsByNameAndCountryCode(anyString(), anyString());
        verifyNoMoreInteractions(cityDao);
    }

}