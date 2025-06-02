package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.auth.repository.UserRepository;
import finalProject.fishingLogTracker.fishingTracker.dto.GroupInvitationDecisionRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.GroupInvitationRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.GroupInvitationResponse;
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
            final Long id) {

        if (request.invitedUserId().equals(id)) {
            throw new SelfInvitationException("User cannot invite themselves to a group.");
        }

        if (groupInvitationRepository.existsByGroupIdAndInvitedUserId(
                request.groupId(),
                request.invitedUserId())) {
            throw new DuplicateInvitationException("User already has a pending invitation to this group.");
        }



        var invitedByUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        var invitedUser = userRepository.findById(request.invitedUserId())
                .orElseThrow(()-> new UserNotFoundException("User not found with id: " + request.invitedUserId()));

        var group = groupRepository.findById(request.groupId())
                .orElseThrow(()->new GroupNotFoundException("Group not found with id: " + request.groupId()));

        boolean isCreator = group.getCreator().getId().equals(id);

        boolean isAdmin = group.getMemberships().stream()
                .anyMatch(m -> m.getUser().getId().equals(id) && m.getRole() == GroupRole.ADMIN);

        if (!isCreator && !isAdmin) {
            throw new InviterNotAdminOrCreator("You are not allowed to invite users to this group.");
        }


        var invitation = GroupInvitation.builder()
                .invitedUser(invitedUser)
                .group(group)
                .invitedBy(invitedByUser)
                .status(InvitationStatus.PENDING)
                .invitedAt(LocalDateTime.now())
                .build();

        var savedInvitation = groupInvitationRepository.save(invitation);

        return groupInvitationMapper.toResponse(savedInvitation);
    }

    public GroupInvitationResponse processInvitationDecision(
           final Long userId,
           final GroupInvitationDecisionRequest request) {

        var invitation = groupInvitationRepository.findById(request.invitationId())
                .orElseThrow(() -> new InvitationNotFoundException("Invitation not found"));

        if (!invitation.getInvitedUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not allowed to respond to this invitation.");
        }

        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new IllegalStateException("This invitation has already been handled.");
        }

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

}
