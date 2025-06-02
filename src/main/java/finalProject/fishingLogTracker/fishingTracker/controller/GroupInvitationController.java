package finalProject.fishingLogTracker.fishingTracker.controller;

import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.fishingTracker.dto.GroupInvitationDecisionRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.GroupInvitationRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.GroupInvitationResponse;
import finalProject.fishingLogTracker.fishingTracker.service.GroupInvitationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/group-invitation")
public class GroupInvitationController {

    private final GroupInvitationService groupInvitationService;

    @PostMapping
    public ResponseEntity<GroupInvitationResponse> sendGroupInvitation(
            @RequestBody final GroupInvitationRequest groupInvitationRequest,
            @AuthenticationPrincipal final User user
    ) {
        return ResponseEntity.ok(
                groupInvitationService.sendGroupInvitation(groupInvitationRequest, user.getId()));
    }

    @PostMapping("/decision")
    public ResponseEntity<GroupInvitationResponse> decideOnGroupInvitation(
            @AuthenticationPrincipal final User user,
            @RequestBody final GroupInvitationDecisionRequest request
    ) {
        GroupInvitationResponse response = groupInvitationService.processInvitationDecision(user.getId(), request);
        return ResponseEntity.ok(response);
    }



}
