package finalProject.fishingLogTracker.auth.service;


import finalProject.fishingLogTracker.auth.dto.AuthenticationRequest;
import finalProject.fishingLogTracker.auth.dto.AuthenticationResponse;
import finalProject.fishingLogTracker.auth.dto.RegisterRequest;
import finalProject.fishingLogTracker.auth.enums.Role;
import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling user authentication and registration logic.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    /**
     * Registers a new user based on the provided request data.
     *
     * @param request the registration request containing user details
     * @return a message indicating success or failure
     */

    public String register(RegisterRequest request) {
        log.info("Attempting to register user: {}", request.username());

        var userIsExist = repository.findByUsername(request.username());
        if (userIsExist.isPresent()) {
            log.warn("Registration failed: Username '{}' already exists", request.username());
            return "Registration error: Username already exists";
        }

        var user = User.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ROLE_USER)
                .photoUrl("profile.png")
                .build();

        repository.save(user);

        log.info("User registered successfully: {}", request.username());
        return "You have successfully registered";
    }

    /**
     * Authenticates a user using the provided credentials and returns a JWT token.
     *
     * @param request the authentication request containing login credentials
     * @return a response containing the generated JWT token
     * @throws UsernameNotFoundException if the username does not exist
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("Attempting authentication for user: {}", request.username());

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        var user = repository.findByUsername(request.username())
                .orElseThrow(() -> {
                    log.warn("Authentication failed: username '{}' not found", request.username());
                    return new UsernameNotFoundException("Username not found " + request.username());
                });

        var jwtToken = jwtService.generateToken(user);
        log.info("Authentication successful for user: {}", request.username());

        return new AuthenticationResponse(jwtToken);
    }
}
