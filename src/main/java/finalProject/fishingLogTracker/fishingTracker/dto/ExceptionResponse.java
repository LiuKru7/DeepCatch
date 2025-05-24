package finalProject.fishingLogTracker.fishingTracker.dto;

import java.time.LocalDateTime;


public record ExceptionResponse(
        String message,
        String status,
        LocalDateTime timestamp
) {}
