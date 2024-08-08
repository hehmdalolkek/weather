package ru.hehmdalolkek.weather.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hehmdalolkek.weather.model.City;

@Repository
public interface CityDao extends JpaRepository<City, Long> {

    boolean existsByNameAndCountryCode(String city, String countryCode);

    City findByNameAndCountryCode(String city, String countryCode);

}
