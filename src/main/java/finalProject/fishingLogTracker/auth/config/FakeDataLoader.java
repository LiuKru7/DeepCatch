package finalProject.fishingLogTracker.auth.config;


import finalProject.fishingLogTracker.auth.enums.Role;
import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.auth.repository.UserRepository;
import finalProject.fishingLogTracker.fishingTracker.entity.Friendship;
import finalProject.fishingLogTracker.fishingTracker.enums.FriendshipStatus;
import finalProject.fishingLogTracker.fishingTracker.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component("authFakeDataLoader")
@RequiredArgsConstructor
public class FakeDataLoader implements CommandLineRunner {

    final private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FriendshipRepository friendshipRepository;

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

        var user4 = User.builder()
                .firstname("user1")
                .lastname("user1")
                .email("user@user1.lt")
                .role(Role.ROLE_USER)
                .username("user1")
                .password(passwordEncoder.encode("user1"))
                .build();

        userRepository.save(user4);

        var user5 = User.builder()
                .firstname("user2")
                .lastname("user2")
                .email("user@user2.lt")
                .role(Role.ROLE_USER)
                .username("user2")
                .password(passwordEncoder.encode("user2"))
                .build();

        userRepository.save(user5);

        var friends = Friendship.builder()
                .sender(userRepository.findByUsername("user").orElseThrow(RuntimeException::new))
                .receiver(userRepository.findByUsername("user1").orElseThrow(RuntimeException::new))
                .status(FriendshipStatus.ACCEPTED)
                .build();

        friendshipRepository.save(friends);

        var friends2 = Friendship.builder()
                .sender(userRepository.findByUsername("user").orElseThrow(RuntimeException::new))
                .receiver(userRepository.findByUsername("user2").orElseThrow(RuntimeException::new))
                .status(FriendshipStatus.ACCEPTED)
                .build();

        friendshipRepository.save(friends2);

    }
}
