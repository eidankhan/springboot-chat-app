package whatsthat.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import whatsthat.app.dto.UserDTO;
import whatsthat.app.dto.UserWithContactsDTO;
import whatsthat.app.entity.Contact;
import whatsthat.app.entity.User;
import whatsthat.app.mapper.UserWithContactsMapper;
import whatsthat.app.service.BlockedUserService;
import whatsthat.app.service.ContactService;
import whatsthat.app.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class ContactsController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private BlockedUserService blockedUserService;

    @GetMapping("/contacts")
    public ResponseEntity<?> getAllContacts(){
        User loggedInUser = userService.getLoggedInUser();
        List<Contact> contactsByUser = contactService.getAllContactsByUser(loggedInUser);
        UserWithContactsDTO userWithContactsDTO = UserWithContactsMapper.INSTANCE.toUserWithContactsDTO(loggedInUser, contactsByUser);
        if (userWithContactsDTO != null && userWithContactsDTO.getContacts().size() < 1)
            return ResponseEntity.notFound().build();
        if (userWithContactsDTO != null && userWithContactsDTO.getContacts().size() > 0)
            return ResponseEntity.ok(userWithContactsDTO.getContacts());
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{user_id}/contact")
    public ResponseEntity<?> addContact(@PathVariable("user_id") Long userId){
        Contact contact = contactService.addContact(userId);
        if(contact!= null){
            return ResponseEntity.ok("New contact added in your contact list");
        }
        return ResponseEntity.badRequest().body("Unable to add contact in your contact list");
    }

    @DeleteMapping("/user/{user_id}/contact")
    public ResponseEntity<?> deleteContact(@PathVariable("user_id") Long userId){
        return contactService.removeContact(userId) ?
                ResponseEntity.ok("Contact deleted from your contact list") :
                ResponseEntity.badRequest().body("Unable to delete contact from your contact list");
    }

    @GetMapping("/blocked")
    public ResponseEntity<?> getAllBlockedUsers(){
        List<Map<String, Object>> blockedUsers = blockedUserService.findAllBlockedUsers();
        return blockedUsers.size() > 0 ?
                ResponseEntity.ok().body(blockedUsers) :
                ResponseEntity.noContent().build();
    }

    @PostMapping("/user/{user_id}/block")
    public ResponseEntity<?> blockUser(@PathVariable("user_id") Long userId){
        return blockedUserService.addUserToBlockList(userId) ?
                ResponseEntity.ok("User blocked with id: " + userId) :
                ResponseEntity.badRequest().body("Unable to block user with id: " + userId);
    }

    @DeleteMapping("/user/{user_id}/unblock")
    public ResponseEntity<?> unblockUser(@PathVariable("user_id") Long userId){
        return blockedUserService.removeUserFromBlockList(userId)?
                ResponseEntity.ok("User unblocked with id: " + userId) :
                ResponseEntity.badRequest().body("Unable to unblock user with id: " + userId);
    }

}
