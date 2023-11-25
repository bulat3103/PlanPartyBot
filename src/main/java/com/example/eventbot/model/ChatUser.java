package com.example.eventbot.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "chat_user")
@NoArgsConstructor
public class ChatUser {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private ChatEntity chat;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private UserEntity user;

    @ManyToOne
    @Column(name = "role_id")
    private Role role;
}
