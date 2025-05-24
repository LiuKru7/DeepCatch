package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.auth.repository.UserRepository;
import finalProject.fishingLogTracker.fishingTracker.dto.ChatMessage;
import finalProject.fishingLogTracker.fishingTracker.entity.Message;
import finalProject.fishingLogTracker.fishingTracker.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

        private final MessageRepository messageRepository;
        private final UserRepository userRepository;

        public ChatMessage save(ChatMessage chatMessage) {
                log.info("Saving message from {} to {}", chatMessage.getSender(), chatMessage.getReceiver());
                User sender = userRepository.findByUsername(chatMessage.getSender())
                                .orElseThrow(() -> new RuntimeException("Sender not found"));
                User receiver = userRepository.findByUsername(chatMessage.getReceiver())
                                .orElseThrow(() -> new RuntimeException("Receiver not found"));

                Message message = Message.builder()
                                .sender(sender)
                                .receiver(receiver)
                                .content(chatMessage.getContent())
                                .sentAt(chatMessage.getTimestamp())
                                .build();

                messageRepository.save(message);
                log.info("Message saved successfully from {} to {}", chatMessage.getSender(),
                                chatMessage.getReceiver());
                return chatMessage;
        }

        public List<ChatMessage> getConversation(String user1, String user2) {
                log.info("Fetching conversation between {} and {}", user1, user2);
                User u1 = userRepository.findByUsername(user1).orElse(null);
                User u2 = userRepository.findByUsername(user2).orElse(null);

                var messages = messageRepository
                                .findBySenderAndReceiverOrReceiverAndSenderOrderBySentAtAsc(u1, u2, u1, u2);

                return messages.stream()
                                .map(m -> {
                                        ChatMessage msg = new ChatMessage();
                                        msg.setSender(m.getSender().getUsername());
                                        msg.setReceiver(m.getReceiver().getUsername());
                                        msg.setContent(m.getContent());
                                        msg.setTimestamp(m.getSentAt());
                                        return msg;
                                })
                                .toList();
        }
}
