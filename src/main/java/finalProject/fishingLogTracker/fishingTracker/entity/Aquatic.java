package finalProject.fishingLogTracker.fishingTracker.entity;

import finalProject.fishingLogTracker.fishingTracker.enums.AquaticType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Aquatic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private AquaticType aquaticType;

}
