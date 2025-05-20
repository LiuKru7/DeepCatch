package finalProject.fishingLogTracker.fishingTracker.controller;

import finalProject.fishingLogTracker.fishingTracker.dto.FriendshipResponse;
import finalProject.fishingLogTracker.fishingTracker.dto.UserResponse;
import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.fishingTracker.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friendship")
@RequiredArgsConstructor
@Slf4j
public class FriendshipController {

    private final FriendshipService friendshipService;

    /**
     * Retrieves a list of the authenticated user's friends.
     *
     * @param user the authenticated user
     * @return list of user's friends
     */
    @GetMapping("/friends")
    public ResponseEntity<List<UserResponse>> getMyFriends(@AuthenticationPrincipal User user) {
        log.info("User {} requested their friends list", user.getId());
        List<UserResponse> friendList = friendshipService.getFriends(user.getId());
        return ResponseEntity.ok(friendList);
    }

    /**
     * Retrieves a list of all other users (excluding the authenticated user).
     *
     * @param user the authenticated user
     * @return list of usernames
     */
    @GetMapping("/userlist")
    public ResponseEntity<List<String>> getAllUsers(@AuthenticationPrincipal User user) {
        log.info("User {} requested user list", user.getId());
        List<String> allUsers = friendshipService.getAllUsers(user.getId());
        return ResponseEntity.ok(allUsers);
    }

    /**
     * Sends a friendship request to another user by username.
     *
     * @param user     the authenticated user
     * @param username the username to send a request to
     * @return response with friendship request details
     */
    @PostMapping
    public ResponseEntity<FriendshipResponse> addToFriend(
            @AuthenticationPrincipal User user,
            @RequestBody String username) {
        log.info("User {} is sending a friend request to '{}'", user.getId(), username);
        FriendshipResponse addedFriend = friendshipService.sentFriendshipRequest(user.getId(), username);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedFriend);
    }

    /**
     * Retrieves a list of received (incoming) friendship requests.
     *
     * @param user the authenticated user
     * @return list of users who sent requests
     */
    @GetMapping("/received")
    public ResponseEntity<List<UserResponse>> getPendingRequestsReceived(@AuthenticationPrincipal User user) {
        log.info("User {} requested received friendship requests", user.getId());
        List<UserResponse> pendingRequestsReceived = friendshipService.getPendingRequestsReceived(user.getId());
        return ResponseEntity.ok(pendingRequestsReceived);
    }

    /**
     * Retrieves a list of sent (outgoing) friendship requests.
     *
     * @param user the authenticated user
     * @return list of users to whom requests were sent
     */
    @GetMapping("/sent")
    public ResponseEntity<List<UserResponse>> getPendingRequestsSent(@AuthenticationPrincipal User user) {
        log.info("User {} requested sent friendship requests", user.getId());
        List<UserResponse> pendingRequestsSent = friendshipService.getPendingRequestsSent(user.getId());
        return ResponseEntity.ok(pendingRequestsSent);
    }

    /**
     * Accepts a friendship request from another user.
     *
     * @param user     the authenticated user
     * @param friendId the ID of the user whose request is being accepted
     * @return details of the accepted friendship
     */
    @PostMapping("/accept")
    public ResponseEntity<FriendshipResponse> acceptRequest(
            @AuthenticationPrincipal User user,
            @RequestBody Long friendId) {
        log.info("User {} accepted friendship request from user {}", user.getId(), friendId);
        FriendshipResponse acceptedRequest = friendshipService.acceptRequest(user.getId(), friendId);
        return ResponseEntity.status(HttpStatus.OK).body(acceptedRequest);
    }

    /**
     * Removes a user from the authenticated user's friend list.
     *
     * @param user     the authenticated user
     * @param friendId the ID of the friend to remove
     * @return HTTP 204 No Content on success
     */
    @DeleteMapping("/{friendId}")
    public ResponseEntity<String> deleteFriend(
            @AuthenticationPrincipal User user,
            @PathVariable Long friendId) {
        log.info("User {} deleted friend with ID {}", user.getId(), friendId);
        friendshipService.deleteFriend(user.getId(), friendId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
