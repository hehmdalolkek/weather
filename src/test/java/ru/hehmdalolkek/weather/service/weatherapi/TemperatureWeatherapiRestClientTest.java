package ru.hehmdalolkek.weather.service.weatherapi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(TemperatureWeatherapiRestClient.class)
class TemperatureWeatherapiRestClientTest {

    @Autowired
    @Qualifier("weatherapi")
    TemperatureRestClient temperatureRestClient;

    @Autowired
    MockRestServiceServer mockServer;

    @Value("${weather.request.api.weatherapi.base-url}")
    String baseUrl;

    @Test
    @DisplayName("Test getCurrentTemperature() functionality with success response")
    void givenRequest_whenGetCurrentTemperature_thenReturnTemperature() {
        // given
        String countryCode = "US";
        String city = "San Francisco";
        String jsonResponse = """
                        {
                            "current":  {
                                            "temp_c": 23.5
                                        }
                        }
                """;
        mockServer.expect(requestTo(startsWith(baseUrl)))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        // when
        double result = temperatureRestClient.getCurrentTemperature(countryCode, city);

        // then
        assertThat(result).isEqualTo(23.5);
    }

}