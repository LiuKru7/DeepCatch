package finalProject.fishingLogTracker.fishingTracker.mapper;

import finalProject.fishingLogTracker.fishingTracker.dto.FriendshipResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Friendship;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface FriendshipMapper {

    @Mapping(target = "senderUsername", source = "friendship.sender.username")
    @Mapping(target = "receiverUsername", source = "friendship.receiver.username")
    FriendshipResponse toFriendshipResponse(Friendship friendship);

}
