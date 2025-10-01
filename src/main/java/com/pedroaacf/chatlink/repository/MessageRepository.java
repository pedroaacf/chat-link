package com.pedroaacf.chatlink.repository;

import com.pedroaacf.chatlink.model.Message;
import com.pedroaacf.chatlink.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findTop100ByConversationOrderByCreatedAtDesc(Conversation conversation);
}