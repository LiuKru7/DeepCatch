package finalProject.fishingLogTracker.fishingTracker.repository;

import finalProject.fishingLogTracker.fishingTracker.entity.Catch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatchRepository extends JpaRepository <Catch, Long> {
}
