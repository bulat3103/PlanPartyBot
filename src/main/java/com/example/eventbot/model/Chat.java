package com.example.eventbot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "code")
    private Long code;
}
