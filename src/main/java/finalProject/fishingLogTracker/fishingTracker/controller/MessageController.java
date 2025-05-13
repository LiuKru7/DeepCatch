package finalProject.fishingLogTracker.fishingTracker.controller;

import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.fishingTracker.dto.ChatMessage;
import finalProject.fishingLogTracker.fishingTracker.service.MessageService;
import lombok.RequiredArgsConstructor;
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
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/chat")
    @SendTo("/topic/group/messages")
    public ChatMessage sendMessageToGroup(ChatMessage message, Principal principal) {
        message.setSender(principal.getName());
        return messageService.save(message);
    }

    @MessageMapping("/chat")
    public void sendMessage(ChatMessage message, Principal principal) {
        message.setSender(principal.getName());
        messageService.save(message);
        messagingTemplate.convertAndSendToUser(
                message.getReceiver(),
                "/topic/messages",
                message
        );
    }


    @GetMapping("api/messages/{friendUsername}")
    public List<ChatMessage> getConversation(@AuthenticationPrincipal User user, @PathVariable String friendUsername) {
        return messageService.getConversation(user.getUsername(), friendUsername);
    }
}

