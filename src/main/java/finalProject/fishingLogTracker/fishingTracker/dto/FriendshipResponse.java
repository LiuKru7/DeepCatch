package finalProject.fishingLogTracker.fishingTracker.dto;

import finalProject.fishingLogTracker.fishingTracker.enums.FriendshipStatus;

public record FriendshipResponse(
        String senderUsername,
        String receiverUsername,
        FriendshipStatus status

) {
}