package finalProject.fishingLogTracker.fishingTracker.mapper;

import finalProject.fishingLogTracker.fishingTracker.dto.GroupInvitationResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.GroupInvitation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupInvitationMapper {

    @Mapping(target = "invitedUserId", source = "invitedUser.id")
    @Mapping(target = "invitedUsername", source = "invitedUser.username")
    @Mapping(target = "invitedByUsername", source = "invitedBy.username")
    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "groupName", source = "group.name")
    @Mapping(target = "groupDescription", source = "group.description")
    GroupInvitationResponse toResponse(GroupInvitation invitation);
}