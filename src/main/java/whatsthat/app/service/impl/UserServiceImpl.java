package whatsthat.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import whatsthat.app.dto.UserDTO;
import whatsthat.app.entity.User;
import whatsthat.app.mapper.UserMapper;
import whatsthat.app.repository.UserRepository;
import whatsthat.app.service.UserService;

import javax.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

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
}
