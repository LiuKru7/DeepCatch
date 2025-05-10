package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.auth.repository.UserRepository;
import finalProject.fishingLogTracker.fishingTracker.dto.FriendshipResponse;
import finalProject.fishingLogTracker.fishingTracker.dto.UserResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Friendship;
import finalProject.fishingLogTracker.fishingTracker.enums.FriendshipStatus;
import finalProject.fishingLogTracker.fishingTracker.mapper.FriendshipMapper;
import finalProject.fishingLogTracker.fishingTracker.mapper.UserMapper;
import finalProject.fishingLogTracker.fishingTracker.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


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
                .map(f -> f.getSender().equals(id) ? f.getReceiver() : f.getSender())
                .map(userMapper::toUserDto)
                .toList();
    }

    public List<UserResponse> getPendingRequestsReceived(Long id) {
        List<Friendship> all = friendshipRepository.findByReceiverIdAndStatus(id, FriendshipStatus.PENDING);
        return all.stream()
                .map(Friendship::getReceiver)
                .map(userMapper::toUserDto)
                .toList();
    }

    public List<UserResponse> getPendingRequestsSent(Long id) {
        List<Friendship> all = friendshipRepository.findBySenderIdAndStatus(id, FriendshipStatus.PENDING);
        return all.stream()
                .map(Friendship::getSender)
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public FriendshipResponse sentFriendshipRequest(Long senderId, Long receiverId) {
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
                .findBySenderIdAndReceiverIdAndStatus(senderId, friendId, FriendshipStatus.PENDING)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        friendship.setStatus(FriendshipStatus.ACCEPTED);
        return friendshipMapper.toFriendshipResponse(friendshipRepository.save(friendship));
    }

    public String deleteFriend(Long id, Long friendId) {

        return "deleted";
    }
}
