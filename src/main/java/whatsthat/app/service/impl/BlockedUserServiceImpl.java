package whatsthat.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whatsthat.app.entity.BlockedUser;
import whatsthat.app.entity.User;
import whatsthat.app.repository.BlockedUserRepository;
import whatsthat.app.service.BlockedUserService;
import whatsthat.app.service.UserService;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class BlockedUserServiceImpl implements BlockedUserService {

    @Autowired
    private BlockedUserRepository blockedUserRepository;

    @Autowired
    private UserService userService;


    @Override
    public boolean addUserToBlockList(Long blockedUserId) {
        User loggedInUser = userService.getLoggedInUser();
        User blockedUser = userService.findById(blockedUserId);

        if (loggedInUser == null || blockedUser == null) {
            System.err.println("Invalid user or blocked user");
            return false;
        }

        Set<BlockedUser> blockedUsers = loggedInUser.getBlockedUsers();
        BlockedUser blockedUserObj = new BlockedUser(loggedInUser, blockedUser);
        boolean isUserAlreadyBlocked = false;
        for (BlockedUser blocked : blockedUsers) {
            if (blocked.getBlockedUser().equals(blockedUser)) {
                isUserAlreadyBlocked = true;
                break;
            }
        }
        if (!isUserAlreadyBlocked) {
            userService.save(loggedInUser);
            blockedUsers.add(blockedUserObj);
            BlockedUser blockedUser1 = blockedUserRepository.save(blockedUserObj);
            loggedInUser.setBlockedUsers(blockedUsers);
            return true;
        } else {
            System.err.println("User is already in the block list");
            return false;
        }
    }

    @Override
    public boolean removeUserFromBlockList(Long blockedUserId) {
        User loggedInUser = userService.getLoggedInUser();
        User blockedUser = userService.findById(blockedUserId);
        if (loggedInUser == null || blockedUser == null) {
            System.err.println("Invalid user or blocked user");
            return false;
        }
        Optional<BlockedUser> byUserAndBlockedUser = blockedUserRepository.findByUserAndBlockedUser(loggedInUser, blockedUser);
        AtomicBoolean isUserUnblocked = new AtomicBoolean(false);
        byUserAndBlockedUser.ifPresent(user -> {
            blockedUserRepository.delete(user);
            System.out.println("User with id "+ blockedUserId +" unblocked");
            isUserUnblocked.set(true);
        });
        return isUserUnblocked.get();
    }

    @Override
    public List<Map<String, Object>> findAllBlockedUsers() {
        List<Map<String, Object>> blockedContacts = new ArrayList<>();
        User loggedInUser = userService.getLoggedInUser();
        Set<BlockedUser> blockedUsers = blockedUserRepository.findAllByUser(loggedInUser);
        for (BlockedUser blockedUser : blockedUsers) {
            Map<String, Object> blockedContact = new HashMap<>();
            blockedContact.put("id", blockedUser.getBlockedUser().getId());
            blockedContact.put("firstName", blockedUser.getBlockedUser().getFirstName());
            blockedContact.put("lastName", blockedUser.getBlockedUser().getLastName());
            blockedContact.put("email", blockedUser.getBlockedUser().getEmail());
            blockedContacts.add(blockedContact);
        }
        return blockedContacts;
    }
}
