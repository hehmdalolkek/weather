package ru.hehmdalolkek.weather.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hehmdalolkek.weather.dto.CurrentTemperatureResponse;
import ru.hehmdalolkek.weather.dto.DailyTemperatureResponse;
import ru.hehmdalolkek.weather.dto.LocationResponse;
import ru.hehmdalolkek.weather.dto.TemperatureResponse;
import ru.hehmdalolkek.weather.model.Temperature;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TemperatureMapper {

    @Mapping(source = "city.country.code", target = "location.country")
    @Mapping(source = "city.name", target = "location.city")
    @Mapping(source = "temperature", target = "current.temperature")
    @Mapping(source = "dateTime", target = "current.datetime")
    CurrentTemperatureResponse toCurrentTemperatureResponse(Temperature temperature);

    @Mapping(source = "dateTime", target = "datetime")
    TemperatureResponse toTemperatureResponse(Temperature temperature);

    default DailyTemperatureResponse toDailyTemperatureResponse(List<Temperature> temperatureList) {
        if (temperatureList == null || temperatureList.isEmpty()) {
            return null;
        }

        Temperature firstTemperature = temperatureList.getFirst();
        LocationResponse locationResponse = LocationResponse.builder()
                .city(firstTemperature.getCity().getName())
                .country(firstTemperature.getCity().getCountry().getCode())
                .build();

        List<TemperatureResponse> temperatureResponse = temperatureList.stream()
                .map(this::toTemperatureResponse)
                .toList();

        return DailyTemperatureResponse.builder()
                .location(locationResponse)
                .daily(temperatureResponse)
                .build();
    }

}
