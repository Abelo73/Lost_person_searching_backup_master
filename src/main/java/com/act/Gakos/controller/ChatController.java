package com.act.Gakos.controller;

import com.act.Gakos.dto.MessageDto;
import com.act.Gakos.entity.Message;
import com.act.Gakos.entity.User;
import com.act.Gakos.repository.MessageRepository;
import com.act.Gakos.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @MessageMapping("/chat.send")
    @SendTo("/topic/messages")
    public MessageDto sendMessage(@Payload MessageDto messageDto) throws Exception {
        logger.info("Received message send request: {}", messageDto);

        // Validate message content
        if (messageDto.getContent() == null || messageDto.getContent().trim().isEmpty()) {
            logger.error("Message content is empty for senderId: {}", messageDto.getSenderId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message content cannot be empty");
        }

        // Fetch sender and receiver users
        Optional<User> senderOptional = userRepository.findById(messageDto.getSenderId());
        Optional<User> receiverOptional = userRepository.findById(messageDto.getReceiverId());

        if (senderOptional.isEmpty() || receiverOptional.isEmpty()) {
            logger.error("User not found: senderId: {}, receivegbgrId: {}", messageDto.getSenderId(), messageDto.getReceiverId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        User sender = senderOptional.get();
        User receiver = receiverOptional.get();
        logger.info("Sender and receiver found: senderId: {}, receiverId: {}", sender.getId(), receiver.getId());

        // Create and save the message
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(messageDto.getContent());
        message.setIsRead(false);

        Message savedMessage = messageRepository.save(message);
        logger.info("Message saved: {}", savedMessage);

        // Return a MessageDto to send to the topic
        return new MessageDto(savedMessage.getId(), savedMessage.getContent(), savedMessage.getSender().getId(), savedMessage.getReceiver().getId());
    }

    @MessageMapping("/chat.read")
    public void markAsRead(@Payload Long messageId) throws Exception {
        logger.info("Received mark as read request for messageId: {}", messageId);

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> {
                    logger.error("Message not found with id: {}", messageId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found");
                });

        // Check if the message is already read
        if (message.getIsRead()) {
            logger.info("Message with id: {} is already marked as read", messageId);
            return; // Optionally, log that the message was already marked as read
        }

        message.setIsRead(true);
        messageRepository.save(message);
        logger.info("Message with id: {} marked as read", messageId);
    }
}
