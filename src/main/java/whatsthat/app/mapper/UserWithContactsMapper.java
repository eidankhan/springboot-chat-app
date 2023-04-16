package whatsthat.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import whatsthat.app.dto.ContactDTO;
import whatsthat.app.dto.UserWithContactsDTO;
import whatsthat.app.entity.Contact;
import whatsthat.app.entity.User;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface UserWithContactsMapper {

    UserWithContactsMapper INSTANCE = Mappers.getMapper(UserWithContactsMapper.class);

    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "contacts", target = "contacts", qualifiedByName = "toContactDTOList")
    UserWithContactsDTO toUserWithContactsDTO(User user, List<Contact> contacts);

    @Named("toContactDTO")
    default ContactDTO toContactDTO(Contact contact) {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setId(contact.getContactUser().getId());
        contactDTO.setFirstName(contact.getContactUser().getFirstName());
        contactDTO.setLastName(contact.getContactUser().getLastName());
        contactDTO.setEmail(contact.getContactUser().getEmail());
        contactDTO.setAddedOn(contact.getAddedOn());
        return contactDTO;
    }

    @Named("toContactDTOList")
    default List<ContactDTO> toContactDTOList(List<Contact> contacts) {
        List<ContactDTO> contactDTOList = new ArrayList<>();
        for (Contact contact : contacts) {
            contactDTOList.add(toContactDTO(contact));
        }
        return contactDTOList;
    }
}
