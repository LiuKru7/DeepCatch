package finalProject.fishingLogTracker.fishingTracker.dto;

public record UserResponse(
        Long id,
        String username,
        String email,
        String photoUrl,
        String firstname,
        String lastname
) {
}
