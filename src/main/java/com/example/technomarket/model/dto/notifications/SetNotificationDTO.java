package com.example.technomarket.model.dto.notifications;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SetNotificationDTO {
    private String message;
    private LocalDateTime dateTime;
}
