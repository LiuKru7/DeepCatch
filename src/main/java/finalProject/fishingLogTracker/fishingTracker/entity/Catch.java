package finalProject.fishingLogTracker.fishingTracker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import finalProject.fishingLogTracker.auth.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Catch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "species_id")
    private Species species;
    private Double size;
    private Double weight;
    @ManyToOne
    private Aquatic aquatic;
//    private Location location;
//    private Wheather wheather;
    private LocalDateTime time;
//    private Bait bait;
    private String photoUrl;
    private boolean isReleased;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

}
