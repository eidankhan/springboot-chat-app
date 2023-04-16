package whatsthat.app.service;

import org.springframework.stereotype.Service;
import whatsthat.app.dto.UserDTO;
import whatsthat.app.entity.User;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {
    UserDTO save(User user);

    User findById(Long id);

    User findByEmail(String email);

    User getLoggedInUser();

    List<Map<String, Object>> search(String query, String searchIn, Integer limit, Integer offset);
}
