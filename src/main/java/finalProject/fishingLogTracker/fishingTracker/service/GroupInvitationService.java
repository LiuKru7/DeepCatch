package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.auth.repository.UserRepository;
import finalProject.fishingLogTracker.fishingTracker.dto.GroupInvitationDecisionRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.GroupInvitationRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.GroupInvitationResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Group;
import finalProject.fishingLogTracker.fishingTracker.entity.GroupInvitation;
import finalProject.fishingLogTracker.fishingTracker.entity.GroupMembership;
import finalProject.fishingLogTracker.fishingTracker.enums.GroupRole;
import finalProject.fishingLogTracker.fishingTracker.enums.InvitationStatus;
import finalProject.fishingLogTracker.fishingTracker.exception.*;
import finalProject.fishingLogTracker.fishingTracker.mapper.GroupInvitationMapper;
import finalProject.fishingLogTracker.fishingTracker.repository.GroupInvitationRepository;
import finalProject.fishingLogTracker.fishingTracker.repository.GroupMembershipRepository;
import finalProject.fishingLogTracker.fishingTracker.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupInvitationService {

    private final GroupInvitationRepository groupInvitationRepository;
    private final GroupInvitationMapper groupInvitationMapper;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupMembershipRepository groupMembershipRepository;

    public GroupInvitationResponse sendGroupInvitation(
            final GroupInvitationRequest request,
            final Long inviterId) {

        validateInvitationRequest(request, inviterId);

        var users = getUsersOrThrow(inviterId, request.invitedUserId());
        var inviter = users.get(inviterId);
        var invitedUser = users.get(request.invitedUserId());

        var group = getGroupOrThrow(request.groupId());

        checkUserAdminOrCreator(inviterId, group);

        var invitation = GroupInvitation.builder()
                .invitedUser(invitedUser)
                .group(group)
                .invitedBy(inviter)
                .status(InvitationStatus.PENDING)
                .invitedAt(LocalDateTime.now())
                .build();

        return groupInvitationMapper.toResponse(groupInvitationRepository.save(invitation));
    }

    public GroupInvitationResponse processInvitationDecision(
            final Long userId,
            final GroupInvitationDecisionRequest request) {

        var invitation = groupInvitationRepository.findById(request.invitationId())
                .orElseThrow(() -> new InvitationNotFoundException("Invitation not found"));

        validateInvitationDecision(invitation, userId);

        if (request.accepted()) {
            var membership = GroupMembership.builder()
                    .group(invitation.getGroup())
                    .user(invitation.getInvitedUser())
                    .role(GroupRole.MEMBER)
                    .joinedAt(LocalDateTime.now())
                    .build();
            groupMembershipRepository.save(membership);
            invitation.setStatus(InvitationStatus.ACCEPTED);
        } else {
            invitation.setStatus(InvitationStatus.REJECTED);
        }

        groupInvitationRepository.save(invitation);

        return groupInvitationMapper.toResponse(invitation);
    }


    private void validateInvitationRequest(GroupInvitationRequest request, Long inviterId) {
        if (request.invitedUserId().equals(inviterId)) {
            throw new SelfInvitationException("User cannot invite themselves.");
        }
        if (groupInvitationRepository.existsByGroupIdAndInvitedUserId(
                request.groupId(), request.invitedUserId())) {
            throw new DuplicateInvitationException("User already has a pending invitation.");
        }
    }

    private Map<Long, User> getUsersOrThrow(Long inviterId, Long invitedUserId) {
        var users = userRepository.findAllById(List.of(inviterId, invitedUserId))
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        if (!users.containsKey(inviterId)) {
            throw new UserNotFoundException("Inviter not found: " + inviterId);
        }
        if (!users.containsKey(invitedUserId)) {
            throw new UserNotFoundException("Invited user not found: " + invitedUserId);
        }

        return users;
    }

    private Group getGroupOrThrow(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Group not found: " + groupId));
    }

    private void checkUserAdminOrCreator(Long inviterId, Group group) {
        boolean isCreator = group.getCreator().getId().equals(inviterId);
        boolean isAdmin = group.getMemberships().stream()
                .anyMatch(m -> m.getUser().getId().equals(inviterId) && m.getRole() == GroupRole.ADMIN);

        if (!isCreator && !isAdmin) {
            throw new InviterNotAdminOrCreator("Only admins or creator can invite.");
        }
    }

    private void validateInvitationDecision(GroupInvitation invitation, Long userId) {
        if (!invitation.getInvitedUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not allowed to respond to this invitation.");
        }
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new IllegalStateException("Invitation is already handled.");
        }
    }
}

