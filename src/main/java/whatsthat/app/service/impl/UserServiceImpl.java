package whatsthat.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import whatsthat.app.dto.UserDTO;
import whatsthat.app.entity.User;
import whatsthat.app.mapper.UserMapper;
import whatsthat.app.repository.UserRepository;
import whatsthat.app.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDTO save(UserDTO userDTO) {
        User user = UserMapper.INSTANCE.userDTOToUser(userDTO);
        String encryptedPassword = bcryptEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        User createddUser = userRepository.save(user);
        return UserMapper.INSTANCE.userToUserDTO(createddUser);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).isPresent() ? userRepository.findById(id).get(): null;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
