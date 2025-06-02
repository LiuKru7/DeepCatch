package finalProject.fishingLogTracker.fishingTracker.dto;

import java.util.List;

public record GroupResponse(
        Long id,
        String name,
        String description,
        String creatorName,
        List<GroupMembershipResponse> memberships,
        List<GroupInvitationResponse> invitations,
        String imageUrl
) {

}
