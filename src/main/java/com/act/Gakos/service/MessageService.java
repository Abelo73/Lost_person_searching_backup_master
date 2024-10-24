package com.act.Gakos.service;

import com.act.Gakos.dto.MessageDto;
import com.act.Gakos.entity.Message;
import com.act.Gakos.entity.User;
import com.act.Gakos.repository.MessageRepository;
import com.act.Gakos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    public Message sendMessage(Integer senderId, Integer receiverId, String content) {
        Optional<User> sender = userRepository.findById(senderId);
        Optional<User> receiver = userRepository.findById(receiverId);

        if (sender.isPresent() && receiver.isPresent()) {
            Message message = new Message();
            message.setSender(sender.get());
            message.setReceiver(receiver.get());
            message.setContent(content);
            message.setIsRead(false);

            return messageRepository.save(message);
        }
        return null; // You can throw an exception if you want to handle it differently
    }

//    public List<Message> getUnreadMessages(Integer receiverId) {
//        Optional<User> receiver = userRepository.findById(receiverId);
//        return receiver.map(user -> messageRepository.findByReceiverAndIsReadFalse(user)).orElse(null);
//    }

    public List<Message> getUnreadMessages(Long receiverId) {
        return messageRepository.findUnreadMessagesByReceiverId(receiverId);
    }

    public Message markMessageAsRead(Long messageId) {
        Optional<Message> message = messageRepository.findById(messageId);
        if (message.isPresent()) {
            Message msg = message.get();
            msg.setIsRead(true);
            return messageRepository.save(msg);
        }
        return null; // You can throw an exception if you want to handle it differently
    }

    public List<Message> getMessageHistory(Integer senderId, Integer receiverId) {
        return messageRepository.findMessagesBetweenUsers(senderId, receiverId);
    }
}
