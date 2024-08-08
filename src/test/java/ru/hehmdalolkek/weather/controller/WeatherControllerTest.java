package ru.hehmdalolkek.weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.hehmdalolkek.weather.dto.CurrentTemperatureResponse;
import ru.hehmdalolkek.weather.dto.DailyTemperatureResponse;
import ru.hehmdalolkek.weather.dto.LocationResponse;
import ru.hehmdalolkek.weather.dto.TemperatureResponse;
import ru.hehmdalolkek.weather.service.TemperatureService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class WeatherControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TemperatureService temperatureService;

    @Test
    @DisplayName("Test getTemperature() functionality with country and city")
    void givenCountryAndCity_whenGetTemperature_thenReturnCurrentTemperature() throws Exception {
        // given
        String countryCode = "DE";
        String city = "Berlin";
        LocalDateTime datetime = LocalDateTime.of(2024, 8, 7, 12, 0);
        double temperature = 20.5;
        when(temperatureService.getLastTemperatureByCountryCodeAndCity(anyString(), anyString()))
                .thenReturn(CurrentTemperatureResponse.builder()
                        .location(LocationResponse.builder()
                                .country(countryCode)
                                .city(city)
                                .build())
                        .current(TemperatureResponse.builder()
                                .datetime(datetime)
                                .temperature(temperature)
                                .build())
                        .build());

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/weather/temperature")
                .accept(MediaType.APPLICATION_JSON)
                .queryParam("countryCode", countryCode)
                .queryParam("city", city)
                .content(objectMapper.writeValueAsString(CurrentTemperatureResponse.class)));

        // then
        result
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.location.country", is(countryCode)))
                .andExpect(jsonPath("$.location.city", is(city)))
                .andExpect(jsonPath("$.current.datetime",
                        is(datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.current.temperature", is(temperature)));
    }

    @Test
    @DisplayName("Test getTemperature() functionality with country, city and date")
    void givenCountryCityDate_whenGetTemperature_thenReturnDailyTemperature() throws Exception {
        // given
        String countryCode = "DE";
        String city = "Berlin";
        LocalDateTime datetime = LocalDateTime.of(2024, 8, 7, 12, 0);
        LocalDateTime datetime2 = LocalDateTime.of(2024, 8, 7, 14, 0);
        double temperature = 20.5;
        double temperature2 = 23;
        LocationResponse location = LocationResponse.builder()
                .country(countryCode)
                .city(city)
                .build();
        TemperatureResponse temperatureResponse1 = TemperatureResponse.builder()
                .temperature(temperature)
                .datetime(datetime)
                .build();
        TemperatureResponse temperatureResponse2 = TemperatureResponse.builder()
                .temperature(temperature2)
                .datetime(datetime2)
                .build();
        when(temperatureService.getAllTemperaturesByCountryCodeAndCityAndDate(anyString(),
                anyString(), any(LocalDate.class)))
                .thenReturn(DailyTemperatureResponse.builder()
                        .location(location)
                        .daily(List.of(temperatureResponse1, temperatureResponse2))
                        .build());

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/weather/temperature")
                .accept(MediaType.APPLICATION_JSON)
                .queryParam("countryCode", countryCode)
                .queryParam("city", city)
                .queryParam("date", datetime.toLocalDate().toString())
                .content(objectMapper.writeValueAsString(DailyTemperatureResponse.class)));

        // then
        result
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.location.country", is(countryCode)))
                .andExpect(jsonPath("$.location.city", is(city)))
                .andExpect(jsonPath("$.daily[0].datetime",
                        is(datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.daily[1].datetime",
                        is(datetime2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.daily[0].temperature", is(temperature)))
                .andExpect(jsonPath("$.daily[1].temperature", is(temperature2)));
    }

}
