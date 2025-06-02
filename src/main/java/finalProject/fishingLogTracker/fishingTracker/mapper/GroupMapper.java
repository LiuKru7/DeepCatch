package finalProject.fishingLogTracker.fishingTracker.mapper;

import finalProject.fishingLogTracker.fishingTracker.dto.GroupRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.GroupResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { GroupInvitationMapper.class, GroupMembershipMapper.class })
public interface GroupMapper {

    Group toGroup(GroupRequest groupRequest);

    @Mapping(target = "creatorName", source = "creator.username")
    GroupResponse toGroupResponse(Group group);
}
