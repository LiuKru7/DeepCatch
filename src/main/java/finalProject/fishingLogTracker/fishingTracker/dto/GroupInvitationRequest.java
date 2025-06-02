package finalProject.fishingLogTracker.fishingTracker.dto;

public record GroupInvitationRequest(
        Long groupId,
        Long invitedUserId
) {
}
