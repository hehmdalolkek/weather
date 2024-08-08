package ru.hehmdalolkek.weather.service.weatherapi;

import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.hehmdalolkek.weather.exception.TemperatureException;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service("openweathermap")
@RequiredArgsConstructor
public class TemperatureOpenweathermapRestClient implements TemperatureRestClient {

    private final RestClient.Builder builder;

    @Value("${weather.request.api.openweathermap.api-key}")
    private String apiKey;

    @Value("${weather.request.api.openweathermap.base-url}")
    private String baseUrl;

    @Override
    public double getCurrentTemperature(String countryCode, String city) {
        RestClient restClient = this.builder
                .baseUrl(this.baseUrl)
                .build();
        String response = restClient.get()
                .uri(uri -> uri
                        .path("/weather")
                        .queryParam("appid", this.apiKey)
                        .queryParam("units", "metric")
                        .queryParam("q", "%s,%s".formatted(city, countryCode))
                        .build())
                .accept(APPLICATION_JSON)
                .retrieve()
                .body(String.class);
        Number temperature = JsonPath.read(response, "$.main.temp");
        return temperature.doubleValue();
    }

}
