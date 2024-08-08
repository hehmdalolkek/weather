package ru.hehmdalolkek.weather.service.weatherapi;

import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.hehmdalolkek.weather.exception.TemperatureException;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service("weatherbit")
@RequiredArgsConstructor
public class TemperatureWeatherbitRestClient implements TemperatureRestClient {

    private final RestClient.Builder builder;

    @Value("${weather.request.api.weatherbit.api-key}")
    private String apiKey;

    @Value("${weather.request.api.weatherbit.base-url}")
    private String baseUrl;

    @Override
    public double getCurrentTemperature(String countryCode, String city) {
        RestClient restClient = this.builder
                .baseUrl(this.baseUrl)
                .build();
        String response = restClient.get()
                .uri(uri -> uri
                        .path("/current")
                        .queryParam("key", this.apiKey)
                        .queryParam("country", countryCode)
                        .queryParam("city", city)
                        .build())
                .accept(APPLICATION_JSON)
                .retrieve()
                .body(String.class);
        Number temperature = JsonPath.read(response, "$.data[0].temp");
        return temperature.doubleValue();
    }

}
