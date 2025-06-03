package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.fishingTracker.dto.ChatMessage;
import finalProject.fishingLogTracker.fishingTracker.dto.GroupMessageRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.GroupMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupMessageService {

    public List<GroupMessageResponse> getConversation(String username, Long groupId) {
        return null;
    }

    public GroupMessageResponse save(GroupMessageRequest message, String name) {
        return null;
    }
}
