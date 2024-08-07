package ru.hehmdalolkek.weather.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TemperatureResponse {

    private float temperature;

    private LocalDateTime datetime;

}
