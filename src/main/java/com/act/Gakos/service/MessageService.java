package com.act.Gakos.service;

import com.act.Gakos.dto.MessageDto;
import com.act.Gakos.entity.Message;
import com.act.Gakos.entity.User;
import com.act.Gakos.repository.MessageRepository;
import com.act.Gakos.repository.UserRepository;
import com.act.Gakos.response.UnreadMessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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


    // Method to mark messages as read
    public void markMessagesAsRead(Integer receiverId, Integer senderId) {
        List<Message> messages = messageRepository.findByReceiverIdAndSenderIdAndIsReadFalse(receiverId, senderId);
        for (Message message : messages) {
            message.setIsRead(true);
            messageRepository.save(message);
        }
    }

    public List<Message> getMessageHistory(Integer senderId, Integer receiverId) {
        return messageRepository.findMessagesBetweenUsers(senderId, receiverId);
    }

    public int countUnreadMessages(Integer receiverId) {
        return messageRepository.countByReceiverIdAndIsReadFalse(receiverId);
    }

    public long getUnreadMessageCount(Integer senderId, Integer receiverId) {
        return messageRepository.countUnreadMessages(senderId, receiverId);
    }

    public UnreadMessageResponse getUnreadMessageCounts(Long receiverId) {
        // Call the repository method which directly returns the count
        Long totalCount = messageRepository.countUnreadMessagesByReceiverId(receiverId);

        // In case totalCount is null (which means no unread messages for the receiver)
        if (totalCount == null) {
            totalCount = 0L; // Set to 0 if null
        }

        return new UnreadMessageResponse(totalCount.intValue()); // Convert Long to int
    }


}
