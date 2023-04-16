package whatsthat.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import whatsthat.app.entity.BlockedUser;
import whatsthat.app.entity.User;

import java.util.Optional;
import java.util.Set;

@Repository
public interface BlockedUserRepository extends JpaRepository<BlockedUser, Long> {
    Optional<BlockedUser> findByUserAndBlockedUser(User loggedUser, User blockedUser);
    Set<BlockedUser> findAllByUser(User loggedInUser);

}
