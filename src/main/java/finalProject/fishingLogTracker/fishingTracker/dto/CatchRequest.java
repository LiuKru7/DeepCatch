package finalProject.fishingLogTracker.fishingTracker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalProject.fishingLogTracker.fishingTracker.enums.FishingStyle;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CatchRequest(
        @NotNull(message = "Species ID is required")
        Long speciesId,

        @NotNull(message = "Size is required")
        @DecimalMin(value = "0.0", message = "Size must be positive")
        Double size,

        @NotNull(message = "Weight is required")
        @DecimalMin(value = "0.0", message = "Weight must be positive")
        Double weight,

        @NotNull(message = "Aquatic ID is required")
        Long aquaticId,

        @NotNull(message = "Fishing style is required")
        FishingStyle fishingStyle,

        @NotNull(message = "Time is required")
        LocalDateTime time,

        @NotNull(message = "Bait ID is required")
        Long baitId,

        @NotNull(message = "Photo URL is required")
        @Pattern(regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$", message = "Invalid photo URL format")
        String photoUrl,

        @NotNull(message = "Is released is required")
        Boolean isReleased,

        @NotNull(message = "Description is required")
        @NotBlank(message = "Description cannot be empty")
        @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
        String description,

        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Latitude is required")
        @DecimalMin(value = "-90.0", message = "Latitude must be at least -90")
        @DecimalMax(value = "90.0", message = "Latitude must be at most 90")
        Double latitude,

        @NotNull(message = "Longitude is required")
        @DecimalMin(value = "-180.0", message = "Longitude must be at least -180")
        @DecimalMax(value = "180.0", message = "Longitude must be at most 180")
        Double longitude,

        @NotNull(message = "Country is required")
        @NotBlank(message = "Country cannot be empty")
        @Size(min = 2, max = 100, message = "Country must be between 2 and 100 characters")
        @Pattern(regexp = "^[a-zA-Z\\s-]+$", message = "Country can only contain letters, spaces, and hyphens")
        String country,

        @NotNull(message = "District is required")
        @NotBlank(message = "District cannot be empty")
        @Size(min = 2, max = 100, message = "District must be between 2 and 100 characters")
        @Pattern(regexp = "^[a-zA-Z\\s-]+$", message = "District can only contain letters, spaces, and hyphens")
        String district) {
}
