package finalProject.fishingLogTracker.fishingTracker.dto;

import finalProject.fishingLogTracker.fishingTracker.enums.GroupRole;

public record GroupMembershipRequest(
        Long groupId,
        Long UserId,
        GroupRole role

) {
}
