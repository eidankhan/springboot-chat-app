package whatsthat.app.service;

import org.springframework.stereotype.Service;
import whatsthat.app.dto.ConversationDTO;
import whatsthat.app.entity.Conversation;

import java.util.Map;

@Service
public interface ConversationService {
    Conversation startConversation(ConversationDTO conversationDTO) throws Exception;

    Map<String, Object> fetchConversations();

    void addUserToChat(Long chatId, Long userId);

    void removeUserFromChat(Long chatId, Long userId);

    Map<String, Object> fetchConversationById(Long id);

    ConversationDTO updateConversationById(Long id, ConversationDTO conversationDTO);
}

