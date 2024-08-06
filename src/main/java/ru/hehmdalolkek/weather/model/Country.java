package ru.hehmdalolkek.weather.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "country")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Country implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "country", orphanRemoval = true)
    private Set<City> cities = new HashSet<>();

    public void addCity(City city) {
        this.cities.add(city);
        city.setCountry(this);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        Country country = (Country) object;
        return Objects.equals(this.code, country.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.code);
    }

}
