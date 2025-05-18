package finalProject.fishingLogTracker.fishingTracker.dto;

import jakarta.validation.constraints.NotBlank;

public record SpeciesRequest(

        @NotBlank(message = "Name must not be blank")
        String name,

        @NotBlank(message = "Latin name must not be blank")
        String latinName
) {
}
