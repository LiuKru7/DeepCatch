package finalProject.fishingLogTracker.fishingTracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SpeciesRequest(
        @NotBlank(message = "Name cannot be empty")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        String name,

        @NotBlank(message = "Latin name cannot be empty")
        @Size(min = 2, max = 50, message = "Latin name must be between 2 and 50 characters")
        String latinName) {
}
