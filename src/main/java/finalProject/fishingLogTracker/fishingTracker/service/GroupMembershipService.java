package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.auth.repository.UserRepository;
import finalProject.fishingLogTracker.fishingTracker.dto.GroupMembershipRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.GroupMembershipResponse;
import finalProject.fishingLogTracker.fishingTracker.enums.GroupRole;
import finalProject.fishingLogTracker.fishingTracker.exception.GroupNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.exception.InviterNotAdminOrCreator;
import finalProject.fishingLogTracker.fishingTracker.exception.MembershipNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.exception.UserNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.mapper.GroupMembershipMapper;
import finalProject.fishingLogTracker.fishingTracker.repository.GroupMembershipRepository;
import finalProject.fishingLogTracker.fishingTracker.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupMembershipService {

    private final UserRepository userRepository;
    private final GroupMembershipRepository groupMembershipRepository;
    private final GroupRepository groupRepository;
    private final GroupMembershipMapper groupMembershipMapper;

    public void deleteGroupMember(Long adminId, GroupMembershipRequest request) {
        validateAdminPermissions(adminId, request);

        var membership = groupMembershipRepository
                .findByUserIdAndGroupId(request.UserId(), request.groupId())
                .orElseThrow(() -> new MembershipNotFoundException("User is not a member of this group."));

        groupMembershipRepository.deleteById(membership.getId());
    }


    private void validateAdminPermissions(Long adminId, GroupMembershipRequest request) {
        if (adminId.equals(request.UserId())) {
            throw new IllegalArgumentException("Admin cannot modify their own role or remove themselves.");
        }

        var users = userRepository.findAllById(List.of(adminId, request.UserId()))
                .stream().collect(Collectors.toMap(User::getId, Function.identity()));

        if (!users.containsKey(adminId)) {
            throw new UserNotFoundException("Admin user not found: " + adminId);
        }
        if (!users.containsKey(request.UserId())) {
            throw new UserNotFoundException("Target user not found: " + request.UserId());
        }

        var group = groupRepository.findById(request.groupId())
                .orElseThrow(() -> new GroupNotFoundException("Group not found: " + request.groupId()));

        boolean isCreator = group.getCreator().getId().equals(adminId);
        boolean isAdmin = group.getMemberships().stream()
                .anyMatch(m -> m.getUser().getId().equals(adminId) && m.getRole() == GroupRole.ADMIN);

        if (!isCreator && !isAdmin) {
            throw new InviterNotAdminOrCreator("Only admins or creator can manage group members.");
        }
    }

    public GroupMembershipResponse changeRole(Long adminId, GroupMembershipRequest request) {
        validateAdminPermissions(adminId, request);

        var membership = groupMembershipRepository
                .findByUserIdAndGroupId(request.UserId(), request.groupId())
                .orElseThrow(() -> new MembershipNotFoundException("User is not a member of this group."));

        membership.setRole(request.role());
        return groupMembershipMapper.toResponse(groupMembershipRepository.save(membership));
    }
}
