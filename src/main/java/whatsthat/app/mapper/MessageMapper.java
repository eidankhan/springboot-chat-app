package whatsthat.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import whatsthat.app.dto.MessageDTO;
import whatsthat.app.entity.Message;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "message", target = "message")
    @Mapping(source = "sentAt", target = "sentAt")
    @Mapping(source = "sender", target = "sender")
    MessageDTO toDto(Message message);

}
