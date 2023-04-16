package whatsthat.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import whatsthat.app.dto.UserDTO;
import whatsthat.app.entity.Contact;
import whatsthat.app.entity.User;
import whatsthat.app.mapper.UserMapper;
import whatsthat.app.repository.UserRepository;
import whatsthat.app.service.ContactService;
import whatsthat.app.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Lazy
    @Autowired
    private ContactService contactService;

    @Override
    public UserDTO save(User user) {
        String encryptedPassword = bcryptEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        User createddUser = userRepository.save(user);
        return UserMapper.INSTANCE.userToUserDTO(createddUser);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).isPresent() ?
            userRepository.findById(id).get(): null;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("No user is logged in");
        }
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            System.out.println("User not found");
            throw new EntityNotFoundException("User not found");
        }
        return user;
    }

    @Override
    public List<Map<String, Object>> search(String query, String searchIn, Integer limit, Integer offset) {
        List<User> result = new ArrayList<>();
        User loggedInUser = getLoggedInUser();
        List<User> users = new ArrayList<>();
        if (searchIn != null && searchIn.equals("contacts")) {
            List<Contact> contactsByUser = contactService.getAllContactsByUser(loggedInUser);
            for (Contact contact : contactsByUser) {
                if (contact.getContactUser() != null) {
                    users.add(contact.getContactUser());
                }
            }
        } else {
            users = userRepository.findAll();
        }

        for (User user : Objects.requireNonNull(users)) {
            if (match(query, user)) {
                result.add(user);
            }
        }
        return paginate(result, limit, offset);
    }

    private boolean match(String query, User user) {
        if (query == null || query.isEmpty()) {
            return true;
        }

        String searchTerm = query.toLowerCase();

        return user.getFirstName().toLowerCase().contains(searchTerm)
                || user.getLastName().toLowerCase().contains(searchTerm)
                || user.getEmail().toLowerCase().contains(searchTerm);
    }

    private List<Map<String, Object>> paginate(List<User> users, Integer limit, Integer offset) {
        List<Map<String, Object>> result = new ArrayList<>();

        if (limit == null || limit <= 0) {
            limit = 20; // default limit
        }

        if (offset == null || offset < 0) {
            offset = 0; // default offset
        }

        int startIndex = offset * limit;

        if (startIndex >= users.size()) {
            return Collections.emptyList(); // no results to return
        }

        int endIndex = Math.min(startIndex + limit, users.size());

        for(User user : users) {
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("firstName", user.getFirstName());
            userData.put("lastName", user.getLastName());
            userData.put("email", user.getEmail());
            result.add(userData);
        }
        return result.subList(startIndex, endIndex);
    }
}
