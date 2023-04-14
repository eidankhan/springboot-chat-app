package whatsthat.app.service;

import org.springframework.stereotype.Service;
import whatsthat.app.entity.Contact;
import whatsthat.app.entity.User;

import java.util.List;

@Service
public interface ContactService {

    Contact addContact(Long userId);
    List<Contact> getAllContactsByUser(User loggedInUser);

    boolean removeContact(Long userId);




}
