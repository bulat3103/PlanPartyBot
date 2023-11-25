package com.example.eventbot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "telegram_id")
    private Long telegramId;

    @Column(name = "user_tag")
    private String userTag;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;
}
