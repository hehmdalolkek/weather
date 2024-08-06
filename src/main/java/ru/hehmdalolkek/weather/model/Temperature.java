package ru.hehmdalolkek.weather.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "temperature")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Temperature implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "temperature", nullable = false)
    private Float temperature;

    @CreationTimestamp
    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "city_id", foreignKey = @ForeignKey(name = "city_country__country_id__fk"))
    private City city;

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Temperature temperature = (Temperature) object;
        return Objects.equals(this.temperature, temperature.temperature)
                && Objects.equals(this.dateTime, temperature.dateTime)
                && Objects.equals(this.city, temperature.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(temperature, dateTime, city);
    }

}
