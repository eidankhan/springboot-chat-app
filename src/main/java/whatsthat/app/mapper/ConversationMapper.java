package whatsthat.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import whatsthat.app.dto.ConversationDTO;
import whatsthat.app.dto.UserDTO;
import whatsthat.app.entity.Conversation;
import whatsthat.app.entity.User;

@Mapper
public interface ConversationMapper {
    ConversationMapper INSTANCE = Mappers.getMapper(ConversationMapper.class);


    @Mapping(source = "participants", target = "participants", qualifiedByName = "toUserDTO")
    ConversationDTO toDto(Conversation conversation);

    @Named("toUserDTO")
    default UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }
}
