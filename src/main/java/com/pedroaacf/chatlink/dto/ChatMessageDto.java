package com.pedroaacf.chatlink.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    @NotBlank(message = "senderName cannot be blank")
    private String senderName;

    @NotBlank(message = "content cannot be blank")
    private String content;
}
