package com.pedroaacf.chatlink.service;

import com.pedroaacf.chatlink.model.Conversation;
import com.pedroaacf.chatlink.repository.ConversationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;

    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Transactional
    public Conversation createConversation(long hoursToExpire) {
        Conversation c = Conversation.builder()
                .id(UUID.randomUUID())
                .token(UUID.randomUUID().toString())
                .createdAt(Instant.now())
                .expiresAt(hoursToExpire > 0 ? Instant.now().plusSeconds(hoursToExpire * 3600) : null)
                .active(true)
                .build();

        return conversationRepository.save(c);
    }


    @Transactional(readOnly = true)
    public Optional<Conversation> findByToken(String token) {
        return conversationRepository.findByToken(token);
    }

    @Transactional(readOnly = true)
    public Conversation findByTokenOrNull(String token) {
        return conversationRepository.findByToken(token).orElse(null);
    }
}