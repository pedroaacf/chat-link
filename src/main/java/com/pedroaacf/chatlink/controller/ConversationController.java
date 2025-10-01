package com.pedroaacf.chatlink.controller;

import com.pedroaacf.chatlink.dto.MessageResponseDto;
import com.pedroaacf.chatlink.mapper.MessageMapper;
import com.pedroaacf.chatlink.model.Conversation;
import com.pedroaacf.chatlink.service.ConversationService;
import com.pedroaacf.chatlink.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationService conversationService;
    private final MessageService messageService;

    public ConversationController(ConversationService conversationService, MessageService messageService) {
        this.conversationService = conversationService;
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<?> createConversation(@RequestParam(defaultValue = "24") long expireHours) {
        Conversation c = conversationService.createConversation(expireHours);
        // Link relativo para abrir a UI estática (index.html) com token na querystring
        String link = "/index.html?token=" + c.getToken();
        return ResponseEntity.ok().body(java.util.Map.of(
                "id", c.getId(),
                "token", c.getToken(),
                "link", link
        ));
    }

    @GetMapping("/{token}/messages")
    public List<MessageResponseDto> getMessages(@PathVariable String token) {
        return messageService.getLatestMessages(token).stream()
                .map(MessageMapper::toDto)
                .collect(Collectors.toList());
    }
}
