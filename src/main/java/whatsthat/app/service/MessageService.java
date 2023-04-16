package whatsthat.app.service;

import org.springframework.stereotype.Service;
import whatsthat.app.dto.MessageDTO;

@Service
public interface MessageService {
    boolean sendMessage(Long conversationId, MessageDTO messageDTO);

    boolean updateMessage(Long conversationId, Long messageId, MessageDTO messageDTO);

    boolean deleteMessage(Long conversationId, Long messageId);
}
