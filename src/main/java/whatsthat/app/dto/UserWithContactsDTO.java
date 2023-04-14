package whatsthat.app.dto;

import java.util.List;

public class UserWithContactsDTO {
    private UserDTO user;
    private List<ContactDTO> contacts;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<ContactDTO> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactDTO> contacts) {
        this.contacts = contacts;
    }
}
