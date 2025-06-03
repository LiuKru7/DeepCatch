package finalProject.fishingLogTracker.fishingTracker.dto;

import java.time.LocalDateTime;

public record GroupMessageResponse(
    Long id,
    String senderUsername,
    String content,
    LocalDateTime sentAt
) {}
