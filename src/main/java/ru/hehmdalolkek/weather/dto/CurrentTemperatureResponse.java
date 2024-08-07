package ru.hehmdalolkek.weather.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrentTemperatureResponse {

    private LocationResponse location;

    private TemperatureResponse current;

}
