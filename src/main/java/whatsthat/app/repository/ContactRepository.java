package whatsthat.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import whatsthat.app.entity.Contact;
import whatsthat.app.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByUser(User loggedInUser);
    Optional<Contact> findByUserAndContactUser(User user, User contactUser);

}
