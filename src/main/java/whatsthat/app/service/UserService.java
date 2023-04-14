package whatsthat.app.service;

import org.springframework.stereotype.Service;
import whatsthat.app.dto.UserDTO;
import whatsthat.app.entity.User;

@Service
public interface UserService {
    UserDTO save(User user);

    User findById(Long id);

    User findByEmail(String email);

    User getLoggedInUser();
}
