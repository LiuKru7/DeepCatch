package finalProject.fishingLogTracker.fishingTracker.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessage {
    private String sender;
    private String receiver;
    private String content;
    private LocalDateTime timestamp;

    public ChatMessage() {
        this.timestamp = LocalDateTime.now();
    }

}