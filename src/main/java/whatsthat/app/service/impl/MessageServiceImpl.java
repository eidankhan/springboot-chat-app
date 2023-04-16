package whatsthat.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whatsthat.app.dto.MessageDTO;
import whatsthat.app.entity.Conversation;
import whatsthat.app.entity.Message;
import whatsthat.app.entity.User;
import whatsthat.app.repository.ConversationRepository;
import whatsthat.app.repository.MessageRepository;
import whatsthat.app.service.MessageService;
import whatsthat.app.service.UserService;

import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ConversationRepository conversationRepository;

    @Override
    public boolean sendMessage(Long conversationId, MessageDTO messageDTO) {
        User loggedInUser = userService.getLoggedInUser();
        Optional<Conversation> conversation = conversationRepository.findById(conversationId);
        if (conversation.isPresent()) {
            Conversation selectedConversation = conversation.get();
            if(selectedConversation.getParticipants().contains(loggedInUser)){
                Message message = new Message();
                message.setMessage(messageDTO.getMessage());
                message.setSender(loggedInUser);
                message.setConversation(selectedConversation);
                messageRepository.save(message);
                System.out.println("Message sent successfully");
                return true;
            }
            System.err.println("User is not a participant of the conversation");
        }
        return false;
    }

    @Override
    public boolean updateMessage(Long conversationId, Long messageId, MessageDTO messageDTO) {
        Optional<Conversation> conversation = conversationRepository.findById(conversationId);
        if (conversation.isPresent()) {
            Conversation selectedConversation = conversation.get();
            Optional<Message> message = messageRepository.findById(messageId);
            if (message.isPresent()) {
                Message selectedMessage = message.get();
                if (selectedConversation.getParticipants().contains(selectedMessage.getSender())) {
                    selectedMessage.setMessage(messageDTO.getMessage());
                    messageRepository.save(selectedMessage);
                    System.out.println("Message updated successfully");
                    return true;
                }
                System.err.println("User is not a participant of the conversation");
            }
        }
        return false;
    }

    @Override
    public boolean deleteMessage(Long conversationId, Long messageId) {
        Optional<Conversation> conversation = conversationRepository.findById(conversationId);
        if (conversation.isPresent()) {
            Conversation selectedConversation = conversation.get();
            Optional<Message> message = messageRepository.findById(messageId);
            if (message.isPresent()) {
                Message selectedMessage = message.get();
                if (selectedConversation.getParticipants().contains(selectedMessage.getSender())) {
                    messageRepository.delete(selectedMessage);
                    System.out.println("Message deleted successfully");
                    return true;
                }
                System.err.println("User is not a participant of the conversation");
            }
        }
        return false;
    }
}
