package whatsthat.app.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface BlockedUserService {
    boolean addUserToBlockList(Long blockedUserId);
    boolean removeUserFromBlockList(Long blockedUserId);

    List<Map<String, Object>> findAllBlockedUsers();

}
