package whatsthat.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import whatsthat.app.entity.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}
