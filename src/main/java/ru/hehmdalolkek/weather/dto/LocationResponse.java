package ru.hehmdalolkek.weather.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationResponse {

    private String country;

    private String city;

}
