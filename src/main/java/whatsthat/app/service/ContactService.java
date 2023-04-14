package whatsthat.app.service;

import org.springframework.stereotype.Service;
import whatsthat.app.entity.Contact;
import whatsthat.app.entity.User;

@Service
public interface ContactService {

    public Contact addContact(Long userId);

}
