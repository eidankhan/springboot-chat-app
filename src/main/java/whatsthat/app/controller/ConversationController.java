package whatsthat.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import whatsthat.app.dto.ConversationDTO;
import whatsthat.app.entity.Conversation;
import whatsthat.app.service.ConversationService;

import java.util.Map;
import java.util.Set;

@RestController
public class ConversationController {
    @Autowired
    private ConversationService conversationService;

    @GetMapping("/chat")
    public ResponseEntity<?> fetchConversations(){
        System.out.println("Fetching conversations");
        Set<ConversationDTO> conversations = conversationService.fetchConversations();
        return conversations.size() > 0 ?
                ResponseEntity.ok(conversations) :
                ResponseEntity.noContent().build();
    }

    @PostMapping("/chat")
    public ResponseEntity<String> initConversation(@RequestBody ConversationDTO conversationDTO) throws Exception {
        try {
            Conversation conversation = conversationService.startConversation(conversationDTO);
            return conversation != null ?
                    ResponseEntity.ok("Created") :
                    ResponseEntity.badRequest().body("Bad Request");
        }
        catch (Exception e) {
            System.err.println("Error creating conversation: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Server Error");
        }
    }

    @GetMapping("/chat/{chat_id}")
    public ResponseEntity<?> getChatDetails(@PathVariable Long chat_id) {
        ConversationDTO conversationDTO = conversationService.fetchConversationById(chat_id);
        return conversationDTO!= null?
                ResponseEntity.ok(conversationDTO) :
                ResponseEntity.notFound().build();
    }

    @PatchMapping("/chat/{chatId}")
    public ResponseEntity<?> updateChatDetails(@PathVariable Long chatId, @RequestBody ConversationDTO conversationDTO) {
        ConversationDTO updatedConversationDTO = conversationService.updateConversationById(chatId, conversationDTO);
        return updatedConversationDTO != null ?
                ResponseEntity.ok(updatedConversationDTO) :
                ResponseEntity.notFound().build();
    }

    @PostMapping("/chat/{chatId}/user/{userId}")
    public ResponseEntity<?> addUserToChat(@PathVariable Long chatId, @PathVariable Long userId) {
        conversationService.addUserToChat(chatId, userId);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/chat/{chatId}/user/{userId}")
    public ResponseEntity<?> removeUserFromChat(@PathVariable Long chatId, @PathVariable Long userId) {
        conversationService.removeUserFromChat(chatId, userId);
        return ResponseEntity.ok().build();
    }
}
