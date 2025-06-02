package finalProject.fishingLogTracker.fishingTracker.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import finalProject.fishingLogTracker.fishingTracker.enums.BaitType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Bait {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BaitType baitType;
    private String description;

    @OneToMany(mappedBy = "bait", fetch = FetchType.LAZY)
    private List<Catch> catches = new ArrayList<>();

    public Bait(BaitType baitType, String description) {
        this.baitType = baitType;
        this.description = description;
    }
}
