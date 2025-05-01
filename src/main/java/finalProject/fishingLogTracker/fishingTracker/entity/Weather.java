package finalProject.fishingLogTracker.fishingTracker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double temperature;
    private Double feelsLike;
    private Double humidity;
    private String condition;
    private Double windSpeed;
    private Integer windDirection;
    private Integer pressure;
}
