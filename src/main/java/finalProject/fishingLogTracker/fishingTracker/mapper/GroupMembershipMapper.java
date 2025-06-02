package finalProject.fishingLogTracker.fishingTracker.mapper;

import finalProject.fishingLogTracker.fishingTracker.dto.GroupMembershipResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.GroupMembership;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupMembershipMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.username")
    GroupMembershipResponse toResponse(GroupMembership membership);
}