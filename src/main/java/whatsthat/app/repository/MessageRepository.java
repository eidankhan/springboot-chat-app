package whatsthat.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import whatsthat.app.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
