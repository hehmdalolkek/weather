package ru.hehmdalolkek.weather.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "city")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(optional = false)
    private Country country;

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        City city = (City) object;
        return Objects.equals(this.name, city.name)
                && Objects.equals(this.country, city.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, country);
    }

}
