package finalProject.fishingLogTracker.fishingTracker.controller;

import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.fishingTracker.dto.ChatMessage;
import finalProject.fishingLogTracker.fishingTracker.service.MessageService;
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
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Handles a group chat message sent via WebSocket.
     *
     * @param message   the chat message
     * @param principal the authenticated user
     * @return the saved chat message to be broadcast to the group
     */
    @MessageMapping("/groupChat")
    @SendTo("/topic/group/messages")
    public ChatMessage sendMessageToGroup(ChatMessage message, Principal principal) {
        message.setSender(principal.getName());
        log.info("Group message from {}: {}", principal.getName(), message.getContent());
        return messageService.save(message);
    }

    /**
     * Handles a private chat message sent via WebSocket and forwards it to the receiver.
     *
     * @param message   the chat message
     * @param principal the authenticated user
     */
    @MessageMapping("/chat")
    public void sendMessage(ChatMessage message, Principal principal) {
        message.setSender(principal.getName());
        log.info("Private message from {} to {}: {}", principal.getName(), message.getReceiver(), message.getContent());
        messageService.save(message);
        messagingTemplate.convertAndSendToUser(
                message.getReceiver(),
                "/topic/messages",
                message
        );
    }

    /**
     * Retrieves the conversation between the authenticated user and a specific friend.
     *
     * @param user            the authenticated user
     * @param friendUsername  the friend's username
     * @return list of chat messages between the two users
     */
    @GetMapping("api/messages/{friendUsername}")
    public List<ChatMessage> getConversation(
            @AuthenticationPrincipal User user,
            @PathVariable String friendUsername) {
        log.info("User {} is retrieving conversation with {}", user.getUsername(), friendUsername);
        return messageService.getConversation(user.getUsername(), friendUsername);
    }
}
