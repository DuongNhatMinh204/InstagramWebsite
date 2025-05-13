package com.nminh.websiteinstagram.repository;

import com.nminh.websiteinstagram.entity.Message;
import com.nminh.websiteinstagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.sender = :user1 AND m.receiver = :user2" +
            " OR m.sender = :user2 AND m.receiver = :user1 " +
            "ORDER BY m.timestamp ASC" )
    List<Message> findChatBeetweenUser(User user1, User user2);

    @Query(value = "SELECT * FROM Message m where m.sender_id = :userId OR m.receiver_id = :userId", nativeQuery = true)
    List<Message> findBySenderOrReceiver(Long userId);
}
