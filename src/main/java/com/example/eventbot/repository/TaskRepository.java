package com.example.eventbot.repository;

import com.example.eventbot.model.Event;
import com.example.eventbot.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByEventId(Event event);
}
