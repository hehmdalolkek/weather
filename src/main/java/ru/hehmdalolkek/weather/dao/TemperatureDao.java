package ru.hehmdalolkek.weather.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hehmdalolkek.weather.model.Temperature;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TemperatureDao extends JpaRepository<Temperature, Long> {

    @Query("""
                FROM Temperature t
                LEFT JOIN FETCH t.city ct
                LEFT JOIN FETCH ct.country cntr
                WHERE DATE(t.dateTime) = :date
                AND ct.name = :city
                AND cntr.code = :countryCode
            """)
    List<Temperature> findAllByDateTimeAndCityNameAndCityCountryCode(@Param("city") String city,
                                                                     @Param("countryCode") String countryCode,
                                                                     @Param("date") LocalDate date);

    @Query("""
                FROM Temperature t
                LEFT JOIN FETCH t.city ct
                LEFT JOIN FETCH ct.country cntr
                WHERE ct.name = :city
                AND cntr.code = :countryCode
                AND t.dateTime = (
                        SELECT MAX(t2.dateTime)
                        FROM Temperature t2
                        LEFT JOIN t2.city ct2
                        LEFT JOIN ct2.country cntr2
                        WHERE ct2.name = :city
                        AND cntr2.code = :countryCode
                    )
            """)
    Optional<Temperature> findLastByCityNameAndCityCountryCode(@Param("city") String city,
                                                               @Param("countryCode") String countryCode);

}
