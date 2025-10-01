package com.pedroaacf.chatlink.mapper;

import com.pedroaacf.chatlink.dto.MessageResponseDto;
import com.pedroaacf.chatlink.model.Message;

public class MessageMapper {
    public static MessageResponseDto toDto(Message m) {
        if (m == null) return null;
        return MessageResponseDto.builder()
                .id(m.getId())
                .senderName(m.getSenderName())
                .content(m.getContent())
                .createdAt(m.getCreatedAt())
                .build();
    }
}
