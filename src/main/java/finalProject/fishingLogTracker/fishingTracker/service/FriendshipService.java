package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.auth.repository.UserRepository;
import finalProject.fishingLogTracker.fishingTracker.dto.FriendshipResponse;
import finalProject.fishingLogTracker.fishingTracker.dto.UserResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Friendship;
import finalProject.fishingLogTracker.fishingTracker.enums.FriendshipStatus;
import finalProject.fishingLogTracker.fishingTracker.exception.FriendshipNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.exception.FriendshipRequestException;
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
        log.info("Fetching friends list for user ID: {}", id);
        List<Friendship> all = friendshipRepository.findAcceptedFriendshipsForUser(id);
        return all.stream()
                .map(f -> f.getSender().getId().equals(id) ? f.getReceiver() : f.getSender())
                .map(userMapper::toUserDto)
                .toList();
    }

    public List<UserResponse> getPendingRequestsReceived(Long id) {
        log.info("Fetching pending received friend requests for user ID: {}", id);
        List<Friendship> all = friendshipRepository.findByReceiverIdAndStatus(id, FriendshipStatus.PENDING);
        return all.stream()
                .map(Friendship::getSender)
                .map(userMapper::toUserDto)
                .toList();
    }

    public List<UserResponse> getPendingRequestsSent(Long id) {
        log.info("Fetching pending sent friend requests for user ID: {}", id);
        List<Friendship> all = friendshipRepository.findBySenderIdAndStatus(id, FriendshipStatus.PENDING);
        return all.stream()
                .map(Friendship::getReceiver)
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public FriendshipResponse sentFriendshipRequest(Long senderId, String username) {
        log.info("User ID: {} is sending friend request to user: {}", senderId, username);
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new FriendshipNotFoundException("User not found"));
        var receiverId = user.getId();

        if (Objects.equals(senderId, receiverId)) {
            log.warn("User ID: {} attempted to send friend request to themselves", senderId);
            throw new FriendshipRequestException("Cannot send friend request to yourself");
        }
        if (friendshipRepository.existsBySenderIdAndReceiverIdOrViceVersa(senderId, receiverId)) {
            log.warn("Friendship already exists between users {} and {}", senderId, receiverId);
            throw new FriendshipRequestException("Friendship already exists");
        }

        var friendship = Friendship.builder()
                .sender(userRepository.findById(senderId)
                        .orElseThrow(() -> new FriendshipNotFoundException("Sender not found")))
                .receiver(userRepository.findById(receiverId)
                        .orElseThrow(() -> new FriendshipNotFoundException("Receiver not found")))
                .status(FriendshipStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        log.info("Friend request sent successfully from user ID: {} to user ID: {}", senderId, receiverId);
        return friendshipMapper.toFriendshipResponse(friendshipRepository.save(friendship));
    }

    public FriendshipResponse acceptRequest(Long senderId, Long friendId) {
        log.info("User ID: {} is accepting friend request from user ID: {}", senderId, friendId);
        Friendship friendship = friendshipRepository
                .findBySenderIdAndReceiverIdAndStatus(friendId, senderId, FriendshipStatus.PENDING)
                .orElseThrow(() -> new FriendshipNotFoundException("No pending friend request from user %d to user %d"
                        .formatted(senderId, friendId)));
        friendship.setStatus(FriendshipStatus.ACCEPTED);
        log.info("Friend request accepted successfully between users {} and {}", senderId, friendId);
        return friendshipMapper.toFriendshipResponse(friendshipRepository.save(friendship));
    }

    public void deleteFriend(Long id, Long friendId) {
        log.info("User ID: {} is removing friendship with user ID: {}", id, friendId);
        Friendship friendship = friendshipRepository.findByUsers(id, friendId)
                .orElseThrow(() -> new FriendshipNotFoundException("Friendship not found"));
        friendshipRepository.delete(friendship);
        log.info("Friendship successfully removed between users {} and {}", id, friendId);
    }

    public List<String> getAllUsers(Long id) {
        return userRepository.findAll().stream()
                .filter(user -> !user.getId().equals(id))
                .map(User::getUsername)
                .toList();
    }
}
