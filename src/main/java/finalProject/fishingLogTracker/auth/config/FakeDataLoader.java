package finalProject.fishingLogTracker.auth.config;


import finalProject.fishingLogTracker.auth.enums.Role;
import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component("authFakeDataLoader")
@RequiredArgsConstructor
public class FakeDataLoader implements CommandLineRunner {

    final private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        var user = User.builder()
                .firstname("Slidra")
                .lastname("Slidra")
                .email("info@Slidra.lt")
                .role(Role.ROLE_ADMIN)
                .username("Slidra")
                .password(passwordEncoder.encode("Slidra#312"))
                .build();

        userRepository.save(user);

        var user2 = User.builder()
                .firstname("admin")
                .lastname("admin")
                .email("admin@slidra.lt")
                .role(Role.ROLE_ADMIN)
                .username("admin")
                .password(passwordEncoder.encode("Admin132#"))
                .build();

        userRepository.save(user2);

        var user3 = User.builder()
                .firstname("user")
                .lastname("user")
                .email("user@Slidra.lt")
                .role(Role.ROLE_USER)
                .username("user")
                .password(passwordEncoder.encode("user"))
                .build();

        userRepository.save(user3);

    }
}
