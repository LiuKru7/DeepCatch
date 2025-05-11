package finalProject.fishingLogTracker.fishingTracker.controller;

import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.fishingTracker.dto.ChatMessage;
import finalProject.fishingLogTracker.fishingTracker.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        return messageService.save(message);
    }
    @GetMapping("/messages/{friendUsername}")
    public List<ChatMessage> getConversation(@AuthenticationPrincipal User user, @PathVariable String friendUsername) {
        return messageService.getConversation(user.getUsername(), friendUsername);
    }

}

