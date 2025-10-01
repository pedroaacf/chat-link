package com.pedroaacf.chatlink.service;

import com.pedroaacf.chatlink.model.Conversation;
import com.pedroaacf.chatlink.model.Message;
import com.pedroaacf.chatlink.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationService conversationService;

    public MessageService(MessageRepository messageRepository, ConversationService conversationService) {
        this.messageRepository = messageRepository;
        this.conversationService = conversationService;
    }


    @Transactional
    public Message saveMessage(String token, String senderName, String content) {
        Conversation conversation = conversationService.findByTokenOrNull(token);
        if (conversation == null || !conversation.isActive()) {
            // Lançar runtime exception faz com que a transação seja revertida (rollback)
            throw new IllegalArgumentException("Conversation not found or inactive");
        }

        Message msg = Message.builder()
                .conversation(conversation)
                .senderName(senderName)
                .content(content)
                .build();


        return messageRepository.save(msg);
    }


    @Transactional(readOnly = true)
    public List<Message> getLatestMessages(String token) {
        Conversation conversation = conversationService.findByTokenOrNull(token);
        if (conversation == null) {
            return List.of();
        }
        return messageRepository.findTop100ByConversationOrderByCreatedAtDesc(conversation);
    }
}
