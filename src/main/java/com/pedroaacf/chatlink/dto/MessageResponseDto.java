package com.pedroaacf.chatlink.dto;

import lombok.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponseDto {
    private Long id;
    private String senderName;
    private String content;
    private Instant createdAt;
}
