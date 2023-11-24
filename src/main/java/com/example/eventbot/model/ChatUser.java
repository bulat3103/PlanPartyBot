package com.example.eventbot.model;

import com.example.eventbot.model.ids.ChatUsersId;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "chat_user")
@NoArgsConstructor
public class ChatUser {
    @EmbeddedId
    private ChatUsersId id;

    @ManyToOne
    @Column(name = "role_id")
    private Role roleId;

    public ChatUser(ChatUsersId id, Role roleId) {
        this.id = id;
        this.roleId = roleId;
    }
}
