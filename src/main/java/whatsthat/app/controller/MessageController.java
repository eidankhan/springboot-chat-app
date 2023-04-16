package whatsthat.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import whatsthat.app.dto.MessageDTO;
import whatsthat.app.service.MessageService;

@RestController
@CrossOrigin
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/chat/{chatId}/message")
    public ResponseEntity<?> sendMessage(@PathVariable Long chatId, @RequestBody MessageDTO messageDTO) {
        boolean messageSent = messageService.sendMessage(chatId, messageDTO);
        return messageSent ?
                ResponseEntity.ok("Message sent") :
                ResponseEntity.badRequest().build();
    }

    @PatchMapping("/chat/{chat_id}/message/{message_id}")
    public ResponseEntity<?> updateMessage(@PathVariable("chat_id") Long chatId, @PathVariable("message_id") Long messageId,
                              @RequestBody MessageDTO message){
        boolean messageUpdated = messageService.updateMessage(chatId, messageId, message);
        return messageUpdated ?
                ResponseEntity.ok("Message updated") :
                ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/chat/{chat_id}/message/{message_id}")
    public ResponseEntity<?> deleteMessage(@PathVariable("chat_id") Long chatId, @PathVariable("message_id") Long messageId){
        boolean messageDeleted = messageService.deleteMessage(chatId, messageId);
        return messageDeleted?
                ResponseEntity.ok("Message deleted") :
                ResponseEntity.badRequest().build();
    }
}
