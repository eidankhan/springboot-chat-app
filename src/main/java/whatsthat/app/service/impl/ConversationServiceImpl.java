package whatsthat.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whatsthat.app.dto.ConversationDTO;
import whatsthat.app.dto.MessageDTO;
import whatsthat.app.dto.UserDTO;
import whatsthat.app.entity.Conversation;
import whatsthat.app.entity.Message;
import whatsthat.app.entity.User;
import whatsthat.app.mapper.ConversationMapper;
import whatsthat.app.mapper.MessageMapper;
import whatsthat.app.mapper.UserMapper;
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
            conversation.setCreator(loggedInUser);
            conversationRepository.save(conversation);
            return conversation;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error creating conversation.");
        }    }

    @Override
    public Map<String, Object> fetchConversations() {
        Map<String, Object> data = new HashMap<String, Object>();

        User loggedInUser = userService.getLoggedInUser();
        Set<Conversation> conversations = loggedInUser.getConversations();

        for (Conversation conversation : conversations) {
            data.put("chat_id", conversation.getId());
            data.put("name", conversation.getName());
            data.put("creator", UserMapper.INSTANCE.userToUserDTO(conversation.getCreator()));
            // Get Last message from conversation
            int totalMessages = conversation.getMessages().size();
            data.put("last_message", conversation.getMessages().get(totalMessages-1));
        }
        return data;
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
    public Map<String, Object>  fetchConversationById(Long id) {
        Map<String, Object> data = new HashMap<String, Object>();
        Optional<Conversation> conversation = conversationRepository.findById(id);
        if (conversation.isPresent()) {
            Conversation selectedConversation = conversation.get();
            data.put("chat_id", selectedConversation.getId());
            data.put("name", selectedConversation.getName());
            data.put("creator", UserMapper.INSTANCE.userToUserDTO(selectedConversation.getCreator()));
            // Populate the members of the conversation
            Set<UserDTO> members = new HashSet<>();
            for(User user: selectedConversation.getParticipants())
                members.add(UserMapper.INSTANCE.userToUserDTO(user));
            data.put("members", members);
            // Populating messages
            Set<MessageDTO> messages = new HashSet<>();
            for(Message message: selectedConversation.getMessages())
                messages.add(MessageMapper.INSTANCE.toDto(message));
            data.put("messages", messages);
        }
        return data;
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
