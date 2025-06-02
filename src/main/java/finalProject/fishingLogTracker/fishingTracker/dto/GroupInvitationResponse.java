package finalProject.fishingLogTracker.fishingTracker.dto;

import finalProject.fishingLogTracker.fishingTracker.enums.InvitationStatus;

import java.time.LocalDateTime;

public record GroupInvitationResponse(
        Long groupId,
        String groupName,
        String groupDescription,
        String invitedUsername,
        Long invitedUserId,
        String invitedByUsername,
        InvitationStatus invitationStatus,
        LocalDateTime invitedAt
) {
}
