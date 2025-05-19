package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.auth.repository.UserRepository;
import finalProject.fishingLogTracker.fishingTracker.dto.FriendshipResponse;
import finalProject.fishingLogTracker.fishingTracker.dto.UserResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Friendship;
import finalProject.fishingLogTracker.fishingTracker.enums.FriendshipStatus;
import finalProject.fishingLogTracker.fishingTracker.mapper.FriendshipMapper;
import finalProject.fishingLogTracker.fishingTracker.mapper.UserMapper;
import finalProject.fishingLogTracker.fishingTracker.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final UserMapper userMapper;
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    private final FriendshipMapper friendshipMapper;

    public List<UserResponse> getFriends(Long id) {
        List<Friendship> all = friendshipRepository.findAcceptedFriendshipsForUser(id);
        return all.stream()
                .map(f -> f.getSender().getId().equals(id) ? f.getReceiver() : f.getSender())
                .map(userMapper::toUserDto)
                .toList();
    }

    public List<UserResponse> getPendingRequestsReceived(Long id) {
        List<Friendship> all = friendshipRepository.findByReceiverIdAndStatus(id, FriendshipStatus.PENDING);
        return all.stream()
                .map(Friendship::getSender)
                .map(userMapper::toUserDto)
                .toList();
    }

    public List<UserResponse> getPendingRequestsSent(Long id) {
        List<Friendship> all = friendshipRepository.findBySenderIdAndStatus(id, FriendshipStatus.PENDING);
        return all.stream()
                .map(Friendship::getReceiver)
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public FriendshipResponse sentFriendshipRequest(Long senderId, String username) {
        var user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found"));
        var receiverId = user.getId();

        if (Objects.equals(senderId, receiverId)){
            throw new RuntimeException("Friendship cant be sent");
        }
        if (friendshipRepository.existsBySenderIdAndReceiverIdOrViceVersa(senderId, receiverId)) {
            throw new RuntimeException("Friendship already exists");
        }
        System.out.println("Sender id: " + senderId);
        System.out.println("Receiver id: " + receiverId);
        var friendship = Friendship.builder()
                .sender(userRepository.findById(senderId).orElseThrow(() -> new IllegalArgumentException("Sender not found")))
                .receiver(userRepository.findById(receiverId).orElseThrow(() -> new IllegalArgumentException("Receiver not found")))
                .status(FriendshipStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        return friendshipMapper.toFriendshipResponse(friendshipRepository.save(friendship));
    }

    public FriendshipResponse acceptRequest(Long senderId, Long friendId) {
        Friendship friendship = friendshipRepository
                .findBySenderIdAndReceiverIdAndStatus(friendId, senderId, FriendshipStatus.PENDING)
                .orElseThrow(() -> new RuntimeException("No pending friend request from user %d to user %d"
                        .formatted(senderId, friendId)));
        friendship.setStatus(FriendshipStatus.ACCEPTED);
        return friendshipMapper.toFriendshipResponse(friendshipRepository.save(friendship));
    }


    public void deleteFriend(Long id, Long friendId) {
            Friendship friendship = friendshipRepository.findByUsers(id, friendId)
                    .orElseThrow(() -> new RuntimeException("Friendship not found"));
            friendshipRepository.delete(friendship);
    }

    public List<String> getAllUsers(Long id) {

        return userRepository.findAll().stream()
                .filter(user-> !user.getId().equals(id))
                .map(User::getUsername)
                .toList();
    }
}
