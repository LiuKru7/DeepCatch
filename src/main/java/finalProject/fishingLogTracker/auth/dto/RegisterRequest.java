package finalProject.fishingLogTracker.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Firstname cannot be empty")
        @Size(min = 2, max = 50, message = "Firstname must be between 2 and 50 characters")
        String firstname,

        @NotBlank(message = "Lastname cannot be empty")
        @Size(min = 2, max = 50, message = "Lastname must be between 2 and 50 characters")
        String lastname,

        @NotBlank(message = "Username cannot be empty")
        @Size(min = 5, max = 20, message = "Username must be between 3 and 20 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
        String username,

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter")
        @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter")
        @Pattern(regexp = ".*[0-9].*", message = "Password must contain at least one digit")
        @Pattern(regexp = ".*[!@#$%^&*()].*", message = "Password must contain at least one special character (!@#$%^&*())")
        String password
) {
}

