package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.auth.repository.UserRepository;
import finalProject.fishingLogTracker.fishingTracker.dto.GroupRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.GroupResponse;
import finalProject.fishingLogTracker.fishingTracker.exception.UserNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.mapper.GroupMapper;
import finalProject.fishingLogTracker.fishingTracker.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMapper groupMapper;

    public GroupResponse createNewGroup(GroupRequest groupRequest, Long creatorId) {
        var creatorUser = userRepository.findById(creatorId).orElseThrow(()-> new UserNotFoundException("User not found"));
        var group = groupRepository.save(groupMapper.toGroup(groupRequest));
        group.setCreator(creatorUser);
        return groupMapper.toGroupResponse(groupRepository.save(group));
    }
}
