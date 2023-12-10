package com.example.eventbot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1543657656858678L;

    private Long userId;

    private String message;
}
