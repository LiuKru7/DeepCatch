package finalProject.fishingLogTracker.fishingTracker.controller;

import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.fishingTracker.dto.GroupMembershipRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.GroupMembershipResponse;
import finalProject.fishingLogTracker.fishingTracker.service.GroupMembershipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/group-membership/")
public class GroupMembershipController {

    private final GroupMembershipService groupMembershipService;

    @DeleteMapping
    public ResponseEntity<String> deleteGroupMember(
            @AuthenticationPrincipal final User user,
            @RequestBody final GroupMembershipRequest request
    ) {
        groupMembershipService.deleteGroupMember(user.getId(), request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }


    @PostMapping("/role")
    public ResponseEntity<GroupMembershipResponse> changeMemberRole(
            @AuthenticationPrincipal final User user,
            @RequestBody final GroupMembershipRequest request
    ) {
        return ResponseEntity.ok(groupMembershipService.changeRole(user.getId(), request));
    }
}
