package finalProject.fishingLogTracker.auth.controller;

import finalProject.fishingLogTracker.auth.dto.AuthenticationRequest;
import finalProject.fishingLogTracker.auth.dto.AuthenticationResponse;
import finalProject.fishingLogTracker.auth.dto.RegisterRequest;
import finalProject.fishingLogTracker.auth.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that handles user authentication and registration requests.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService service;

    /**
     * Registers a new user based on the provided registration request data.
     *
     * @param request the registration request containing user details
     * @return a ResponseEntity with a success message
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid final RegisterRequest request) {
        log.info("Attempting to register user with username: {}", request.username());
        String response = service.register(request);
        log.info("User registered successfully with username: {}", request.username());
        return ResponseEntity.ok(response);
    }

    /**
     * Authenticates a user based on the provided authentication request data.
     *
     * @param request the authentication request containing login credentials
     * @return a ResponseEntity containing the authentication response
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody final AuthenticationRequest request) {
        log.info("Attempting to authenticate user with username: {}", request.username());
        AuthenticationResponse response = service.authenticate(request);
        log.info("Authentication successful for user with username: {}", request.username());
        return ResponseEntity.ok(response);
    }
}
