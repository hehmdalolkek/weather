package ru.hehmdalolkek.weather.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TemperatureResponse {

    private double temperature;

    private LocalDateTime datetime;

}
