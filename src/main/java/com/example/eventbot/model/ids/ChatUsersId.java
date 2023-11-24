package com.example.eventbot.model.ids;

import com.example.eventbot.model.Chat;
import com.example.eventbot.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class ChatUsersId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chatId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    public ChatUsersId(Chat chatId, User userId) {
        this.chatId = chatId;
        this.userId = userId;
    }
}
