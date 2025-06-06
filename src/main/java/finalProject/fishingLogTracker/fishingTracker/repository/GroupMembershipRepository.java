package finalProject.fishingLogTracker.fishingTracker.repository;

import finalProject.fishingLogTracker.fishingTracker.entity.GroupMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupMembershipRepository extends JpaRepository<GroupMembership, Long> {

    Optional<GroupMembership> findByUserIdAndGroupId(Long userId, Long groupId);

}
