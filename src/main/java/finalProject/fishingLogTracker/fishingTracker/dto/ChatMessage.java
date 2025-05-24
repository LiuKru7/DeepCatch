package finalProject.fishingLogTracker.fishingTracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessage {
    @NotBlank(message = "Sender cannot be empty")
    private String sender;

    @NotBlank(message = "Receiver cannot be empty")
    private String receiver;

    @NotBlank(message = "Content cannot be empty")
    @Size(min = 1, max = 1000, message = "Message must be between 1 and 1000 characters")
    private String content;

    private LocalDateTime timestamp;

    public ChatMessage() {
        this.timestamp = LocalDateTime.now();
    }

}