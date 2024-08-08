package ru.hehmdalolkek.weather.service.weatherapi;


public interface TemperatureRestClient {

    double getCurrentTemperature(String countryCode, String city);

}
