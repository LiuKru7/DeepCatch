package finalProject.fishingLogTracker.fishingTracker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import finalProject.fishingLogTracker.fishingTracker.enums.AquaticType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "aquatic", fetch = FetchType.LAZY)
    private List<Catch> catches = new ArrayList<>();

    public Aquatic(String name, AquaticType aquaticType) {
        this.name = name;
        this.aquaticType = aquaticType;
    }
}
