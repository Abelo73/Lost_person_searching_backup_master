package com.act.Gakos.repository;

import com.act.Gakos.dto.MessageDto;
import com.act.Gakos.entity.Message;
import com.act.Gakos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {




    @Query(value = "SELECT * FROM message " +
            "WHERE receiver_id = :receiverId AND is_read = false", nativeQuery = true)
    List<Message> findUnreadMessagesByReceiverId(@Param("receiverId") Long receiverId);

    @Query("SELECT m FROM Message m WHERE (m.sender.id = :senderId AND m.receiver.id = :receiverId) " +
            "OR (m.sender.id = :receiverId AND m.receiver.id = :senderId) ORDER BY m.sentAt ASC")
    List<Message> findMessagesBetweenUsers(@Param("senderId") Integer senderId,
                                           @Param("receiverId") Integer receiverId);

    int countByReceiverIdAndIsReadFalse(Integer receiver_id);


    List<Message> findByReceiverIdAndSenderIdAndIsReadFalse(Long receiverId, Long senderId);
}
