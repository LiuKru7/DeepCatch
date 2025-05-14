package finalProject.fishingLogTracker.auth.service;


import finalProject.fishingLogTracker.auth.dto.AuthenticationRequest;
import finalProject.fishingLogTracker.auth.dto.AuthenticationResponse;
import finalProject.fishingLogTracker.auth.dto.RegisterRequest;
import finalProject.fishingLogTracker.auth.enums.Role;
import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public String register(RegisterRequest request) {

        var userIsExist = repository.findByUsername(request.username());
        if (userIsExist.isPresent()) {
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

        return "You have successfully registered";
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        var user = repository.findByUsername(request.username())
                .orElseThrow(()-> new UsernameNotFoundException("Username not found " + request.username() ));

        var jwtToken = jwtService.generateToken(user);

        return new AuthenticationResponse(jwtToken);
    }
}