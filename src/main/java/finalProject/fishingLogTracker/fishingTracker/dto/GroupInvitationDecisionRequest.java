package finalProject.fishingLogTracker.fishingTracker.dto;
import jakarta.validation.constraints.NotNull;

public record GroupInvitationDecisionRequest(
        @NotNull Long invitationId,
        @NotNull Boolean accepted
) {}

