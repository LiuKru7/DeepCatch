package finalProject.fishingLogTracker.fishingTracker.controller;

import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.fishingTracker.dto.GroupRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.GroupResponse;
import finalProject.fishingLogTracker.fishingTracker.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/group")
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupResponse> createNewGroup(
            @AuthenticationPrincipal final User user,
            @RequestBody GroupRequest groupRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(groupService.createNewGroup(groupRequest, user.getId()));
    }
}
