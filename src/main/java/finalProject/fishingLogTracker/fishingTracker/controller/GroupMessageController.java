package finalProject.fishingLogTracker.fishingTracker.controller;

import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.fishingTracker.dto.GroupMessageRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.GroupMessageResponse;
import finalProject.fishingLogTracker.fishingTracker.service.GroupMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GroupMessageController {

    private final GroupMessageService groupMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Handles a group chat message sent via WebSocket.
     *
     * @param message   the chat message
     * @param user the authenticated user
     * @return the saved chat message to be broadcast to the group
     */
    @MessageMapping("/groupChat")
    @SendTo("/topic/group/messages")
    public GroupMessageResponse sendMessageToGroup(final GroupMessageRequest message, final Principal user) {
        log.info("Group message from {}: {}", user.getName(), message.content());
        return groupMessageService.save(message, user.getName());
    }

    @GetMapping("api/group/message/{groupId}")
    public List<GroupMessageResponse> getConversation(
            @AuthenticationPrincipal final User user,
            @PathVariable final Long groupId) {
        log.info("User {} is retrieving conversation with {}", user.getUsername(), groupId);
        return groupMessageService.getConversation(user.getUsername(), groupId);
    }
}
