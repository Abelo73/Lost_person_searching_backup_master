package com.act.Gakos.controller;

import com.act.Gakos.dto.MessageDto;
import com.act.Gakos.entity.Message;
import com.act.Gakos.service.MessageService;
import com.act.Gakos.service.UserService;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

//    @PostMapping("/send")
//    public ResponseEntity<Message> sendMessage(@RequestParam Integer senderId,
//                                               @RequestParam Integer receiverId,
//                                               @RequestBody Map<String, String> payload) {
//
//        String content = payload.get("content");
//        logger.debug("Sending message from user ID {} to user ID {}: {}", senderId, receiverId, content);
//        Message savedMessage = messageService.sendMessage(senderId, receiverId, content);
//        if (savedMessage != null) {
//            logger.info("Message sent successfully: {}", savedMessage);
//            return ResponseEntity.ok(savedMessage);
//        } else {
//            logger.warn("Failed to send message. Sender or receiver not found.");
//            return ResponseEntity.badRequest().body(null);
//        }
//    }

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestParam Integer senderId,
                                               @RequestParam Integer receiverId,
                                               @RequestBody Map<String, String> payload) {

        String content = payload.get("content");
        logger.debug("Sending message from user ID {} to user ID {}: {}", senderId, receiverId, content);

        // Call your message service to send the message
        Message savedMessage = messageService.sendMessage(senderId, receiverId, content);

        if (savedMessage != null) {
            // Update last seen for the sender after sending the message
            userService.updateLastSeen(senderId);

            logger.info("Message sent successfully: {}", savedMessage);
            return ResponseEntity.ok(savedMessage);
        } else {
            logger.warn("Failed to send message. Sender or receiver not found.");
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping("/unread/{receiverId}")
    public ResponseEntity<List<Message>> getUnreadMessages(@PathVariable Long receiverId) {
        logger.debug("Fetching unread messages for receiver ID {}", receiverId);
        List<Message> unreadMessages = messageService.getUnreadMessages(receiverId);

        unreadMessages.forEach(message -> {
            Hibernate.initialize(message.getSender());
        });
        if (unreadMessages != null) {
            logger.info("Unread messages fetched successfully: {}", unreadMessages.size());
            return ResponseEntity.ok(unreadMessages);
        } else {
            logger.warn("Receiver ID {} not found", receiverId);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/read/{messageId}")
    public ResponseEntity<Message> markMessageAsRead(@PathVariable Long messageId) {
        logger.debug("Marking message ID {} as read", messageId);
        Message msg = messageService.markMessageAsRead(messageId);
        if (msg != null) {
            logger.info("Message ID {} marked as read successfully", messageId);
            return ResponseEntity.ok(msg);
        } else {
            logger.warn("Message ID {} not found", messageId);
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/history")
    public ResponseEntity<List<Message>> getMessageHistory(@RequestParam Integer senderId,
                                                           @RequestParam Integer receiverId) {
        logger.debug("Fetching message history between sender ID {} and receiver ID {}", senderId, receiverId);
        List<Message> messageHistory = messageService.getMessageHistory(senderId, receiverId);

        if (!messageHistory.isEmpty()) {
            messageHistory.forEach(message -> {
                Hibernate.initialize(message.getSender());
                Hibernate.initialize(message.getReceiver());
            });
            logger.info("Message history fetched successfully: {} messages found", messageHistory.size());
            return ResponseEntity.ok(messageHistory);
        } else {
            logger.warn("No message history found between sender ID {} and receiver ID {}", senderId, receiverId);
            return ResponseEntity.noContent().build();
        }
    }
}
