package ru.hehmdalolkek.weather.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DailyTemperatureResponse {

    private LocationResponse location;

    private List<TemperatureResponse> daily;

}
