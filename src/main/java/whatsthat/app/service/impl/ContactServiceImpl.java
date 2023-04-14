package whatsthat.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whatsthat.app.entity.Contact;
import whatsthat.app.entity.User;
import whatsthat.app.repository.ContactRepository;
import whatsthat.app.service.ContactService;
import whatsthat.app.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private UserService userService;

    @Autowired
    private ContactRepository contactRepository;
    @Override
    public Contact addContact(Long userId) {
        // Get the logged-in user
        User loggedInUser = userService.getLoggedInUser();

        // Check if the user is trying to add themselves as a contact
        if (userId.equals(loggedInUser.getId())) {
            System.err.println("You can't add yourself as a contact");
            return null;
        }

        // Get the user to add as a contact
        User userToAdd = userService.findById(userId);

        // Check if the logged-in user already has this user in their contacts list
        Optional<Contact> existingContact = contactRepository.findByUserAndContactUser(loggedInUser, userToAdd);
        if (existingContact.isPresent()) {
            System.err.println("You already have this contact");
            return null;
        }

        // Create a new contact and save it to the database
        Contact contact = new Contact();
        contact.setUser(loggedInUser);
        contact.setContactUser(userToAdd);
        return contactRepository.save(contact);
    }

    @Override
    public List<Contact> getAllContactsByUser(User loggedInUser) {
        return contactRepository.findByUser(loggedInUser);
    }

    @Override
    public boolean removeContact(Long contactId) {
        try {
            User loggedInUser = userService.getLoggedInUser();
            User contact = userService.findById(contactId);
            if(loggedInUser != null && contact != null) {
                Optional<Contact> optionalContact = contactRepository.findByUserAndContactUser(loggedInUser, contact);
                if (!optionalContact.isPresent()) {
                    System.err.println("Contact not found");
                    return false;
                }
                contactRepository.delete(optionalContact.get());
                return true;
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

}
