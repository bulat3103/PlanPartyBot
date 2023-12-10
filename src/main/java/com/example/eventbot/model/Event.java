package com.example.eventbot.model;

import com.example.eventbot.model.enums.NotificationLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "level")
    @Enumerated(EnumType.ORDINAL)
    private NotificationLevel level;

    @Column(name = "until")
    private LocalDate until;

    @Column(name = "last_notification")
    private Timestamp lastNotify;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private ChatEntity chatId;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private UserEntity adminId;
}
