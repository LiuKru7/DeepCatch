package finalProject.fishingLogTracker.fishingTracker.repository;

import finalProject.fishingLogTracker.fishingTracker.entity.Catch;
import finalProject.fishingLogTracker.fishingTracker.enums.BaitType;
import finalProject.fishingLogTracker.fishingTracker.enums.FishingStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatchRepository extends JpaRepository<Catch, Long> {
    List<Catch> findByFishingStyle(FishingStyle fishingStyle);

    List<Catch> findByBait_BaitType(BaitType baitType);
}
