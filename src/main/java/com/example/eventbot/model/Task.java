package com.example.eventbot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @Column(name = "event_id")
    private Event eventId;

    @ManyToOne
    @Column(name = "user_id")
    private User userId;

    @Column(name = "name")
    private String name;

    @Column(name = "ordinal")
    private Integer ordinal;

    @Column(name = "done")
    private Boolean done;
}
