package whatsthat.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whatsthat.app.dto.ConversationDTO;
import whatsthat.app.entity.Conversation;
import whatsthat.app.entity.User;
import whatsthat.app.mapper.ConversationMapper;
import whatsthat.app.repository.ConversationRepository;
import whatsthat.app.repository.UserRepository;
import whatsthat.app.service.ConversationService;
import whatsthat.app.service.UserService;

import java.util.*;

@Service
public class ConversationServiceImpl implements ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Conversation startConversation(ConversationDTO conversationDTO) throws Exception {
        try {
            User loggedInUser = userService.getLoggedInUser();
            Conversation conversation = new Conversation(conversationDTO.getName());
            conversation.addParticipant(loggedInUser);
            conversationRepository.save(conversation);
            return conversation;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error creating conversation.");
        }    }

    @Override
    public Set<ConversationDTO> fetchConversations() {
        User loggedInUser = userService.getLoggedInUser();
        Set<Conversation> conversations = loggedInUser.getConversations();
        Set<ConversationDTO> result = new HashSet<>();

        for (Conversation conversation : conversations) {
            result.add(ConversationMapper.INSTANCE.toDto(conversation));
        }
        return result;
    }

    @Override
    public void addUserToChat(Long chatId, Long userId) {
        Optional<Conversation> conversation = conversationRepository.findById(chatId);
        if(conversation.isPresent()) {
            Optional<User> user = userRepository.findById(userId);
            if(user.isPresent()) {
                conversation.get().getParticipants().add(user.get());
                conversationRepository.save(conversation.get());
            }
            else {
                throw new RuntimeException("User not found");
            }
        }
        else {
            throw new RuntimeException("Conversation not found");
        }

    }

    @Override
    public void removeUserFromChat(Long chatId, Long userId) {
        Optional<Conversation> conversation = conversationRepository.findById(chatId);
        if(conversation.isPresent()) {
            Optional<User> user = userRepository.findById(userId);
            if(user.isPresent()) {
                conversation.get().getParticipants().remove(user.get());
                conversationRepository.save(conversation.get());
            }
            else {
                throw new RuntimeException("User not found");
            }
        }
        else {
            throw new RuntimeException("Conversation not found");
        }
    }

    @Override
    public ConversationDTO fetchConversationById(Long id) {
        Optional<Conversation> conversation = conversationRepository.findById(id);
        return conversation.map(ConversationMapper.INSTANCE::toDto).orElse(null);
    }

    @Override
    public ConversationDTO updateConversationById(Long id, ConversationDTO conversationDTO) {
        Optional<Conversation> conversation = conversationRepository.findById(id);
        if(conversation.isPresent()){
            Conversation updatedConversation = conversation.get();
            updatedConversation.setName(conversationDTO.getName());
            return ConversationMapper.INSTANCE.toDto(conversationRepository.save(updatedConversation));
        }
        System.err.println("Conversation with id " + id + " not found");
        return null;
    }
}
