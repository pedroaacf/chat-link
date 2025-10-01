package com.pedroaacf.chatlink.repository;

import com.pedroaacf.chatlink.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {
    Optional<Conversation> findByToken(String token);
}