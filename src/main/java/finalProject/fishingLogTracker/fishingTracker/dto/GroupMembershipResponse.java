package finalProject.fishingLogTracker.fishingTracker.dto;

import finalProject.fishingLogTracker.fishingTracker.enums.GroupRole;

import java.time.LocalDateTime;

public record GroupMembershipResponse(
        Long userId,
        String userName,
        GroupRole role,
        LocalDateTime joinedAt
) {
}
