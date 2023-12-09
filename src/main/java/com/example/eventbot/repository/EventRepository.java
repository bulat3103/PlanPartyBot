package com.example.eventbot.repository;

import com.example.eventbot.model.ChatEntity;
import com.example.eventbot.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findEventByChatId(ChatEntity chat);
}
