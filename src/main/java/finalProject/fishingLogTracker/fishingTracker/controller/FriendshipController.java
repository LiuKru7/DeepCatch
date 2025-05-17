package finalProject.fishingLogTracker.fishingTracker.controller;

import finalProject.fishingLogTracker.fishingTracker.dto.FriendshipResponse;
import finalProject.fishingLogTracker.fishingTracker.dto.UserResponse;
import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.fishingTracker.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friendship")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;

    @GetMapping("/friends")
    public ResponseEntity<List<UserResponse>> getMyFriends(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(friendshipService.getFriends(user.getId()));
    }



    @GetMapping("/userlist")
    public ResponseEntity<List<String>> getAllUsers() {
        return ResponseEntity.ok(friendshipService.getAllUsers());
    }

    @PostMapping()
    public ResponseEntity<FriendshipResponse> addToFriend(@AuthenticationPrincipal User user, @RequestBody Long friendId) {
        return ResponseEntity.ok(friendshipService.sentFriendshipRequest(user.getId(), friendId));
    }
    @GetMapping("/received")
    public ResponseEntity<List<UserResponse>> getPendingRequestsReceived(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(friendshipService.getPendingRequestsReceived(user.getId()));
    }
    @GetMapping("/sent")
    public ResponseEntity<List<UserResponse>> getPendingRequestsSent(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(friendshipService.getPendingRequestsSent(user.getId()));
    }
    @PostMapping("/accept")
    public ResponseEntity<?> acceptRequest(@AuthenticationPrincipal User user, @RequestBody Long friendId) {
        return ResponseEntity.ok(friendshipService.acceptRequest(user.getId(), friendId));
    }
    @DeleteMapping("/{friendId}")
    public ResponseEntity<?> deleteFriend(@AuthenticationPrincipal User user, @PathVariable Long friendId) {
        return ResponseEntity.ok(friendshipService.deleteFriend(user.getId(),friendId));
    }
}
