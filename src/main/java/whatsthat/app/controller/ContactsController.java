package whatsthat.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import whatsthat.app.entity.Contact;
import whatsthat.app.service.ContactService;

@RestController
@CrossOrigin
public class ContactsController {

    @Autowired
    private ContactService contactService;

    @PostMapping("/{user_id}/contact")
    public ResponseEntity<?> addContact(@PathVariable("user_id") Long userId){
        Contact contact = contactService.addContact(userId);
        if(contact!= null){
            return ResponseEntity.ok("New contact added in your contact list");
        }
        return null;
    }
}
