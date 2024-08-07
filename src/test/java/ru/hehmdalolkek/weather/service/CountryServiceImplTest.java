package ru.hehmdalolkek.weather.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hehmdalolkek.weather.dao.CountryDao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceImplTest {

    @InjectMocks
    CountryServiceImpl countryService;

    @Mock
    CountryDao countryDao;

    @Test
    @DisplayName("Test countryExistsByCountryCode() functionality with an existing country code")
    void givenCountryCode_whenCountryExistsByCountryCode_thenReturnTrue() {
        // given
        String countryCode = "RU";
        when(countryDao.existsByCodeIgnoreCase(anyString())).thenReturn(true);

        // when
        boolean result = countryService.countryExistsByCountryCode(countryCode);

        // then
        assertThat(result).isTrue();
        verify(countryDao).existsByCodeIgnoreCase(anyString());
        verifyNoMoreInteractions(countryDao);
    }

    @Test
    @DisplayName("Test countryExistsByCountryCode() functionality with non-existing country code")
    void givenCountryCode_whenCountryExistsByCountryCode_thenReturnFalse() {
        // given
        String countryCode = "WW";
        when(countryDao.existsByCodeIgnoreCase(anyString())).thenReturn(false);

        // when
        boolean result = countryService.countryExistsByCountryCode(countryCode);

        // then
        assertThat(result).isFalse();
        verify(countryDao).existsByCodeIgnoreCase(anyString());
        verifyNoMoreInteractions(countryDao);
    }

}