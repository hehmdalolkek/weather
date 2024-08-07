package ru.hehmdalolkek.weather.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hehmdalolkek.weather.model.Country;

@Repository
public interface CountryDao extends JpaRepository<Country, Long> {

    boolean existsByCodeIgnoreCase(String name);

}
