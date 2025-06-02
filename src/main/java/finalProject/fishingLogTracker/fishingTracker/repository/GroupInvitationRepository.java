package finalProject.fishingLogTracker.fishingTracker.repository;

import finalProject.fishingLogTracker.fishingTracker.entity.GroupInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupInvitationRepository extends JpaRepository<GroupInvitation, Long> {

    boolean existsByGroupIdAndInvitedUserId(Long groupId, Long invitedUserId);

}
