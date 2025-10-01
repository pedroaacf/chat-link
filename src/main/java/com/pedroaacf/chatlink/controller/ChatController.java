package com.pedroaacf.chatlink.controller;

import com.pedroaacf.chatlink.dto.ChatMessageDto;
import com.pedroaacf.chatlink.dto.MessageResponseDto;
import com.pedroaacf.chatlink.mapper.MessageMapper;
import com.pedroaacf.chatlink.model.Message;
import com.pedroaacf.chatlink.service.MessageService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ChatController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final Validator validator;

    public ChatController(MessageService messageService, SimpMessagingTemplate messagingTemplate, Validator validator) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
        this.validator = validator;
    }

    @MessageMapping("/conversation/{token}/message")
    public void handleMessage(@DestinationVariable String token, ChatMessageDto payload) {
        Set<ConstraintViolation<ChatMessageDto>> violations = validator.validate(payload);
        if (!violations.isEmpty()) {
            String errors = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining("; "));
            messagingTemplate.convertAndSend("/topic/conversation." + token + ".error",
                    Map.of("type","validation_error","message", errors));
            return;
        }

        Message saved = messageService.saveMessage(token, payload.getSenderName(), payload.getContent());

        MessageResponseDto dto = MessageMapper.toDto(saved);

        messagingTemplate.convertAndSend("/topic/conversation." + token, dto);
    }
}
