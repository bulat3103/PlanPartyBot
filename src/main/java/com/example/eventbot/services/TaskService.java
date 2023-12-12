package com.example.eventbot.services;

import com.example.eventbot.model.Event;
import com.example.eventbot.model.Task;
import com.example.eventbot.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public void create(Event event, String name, int ordinal) {
        Task task = new Task();
        task.setDone(false);
        task.setName(name);
        task.setOrdinal(ordinal);
        task.setEventId(event);
        taskRepository.save(task);
    }

    public void update(Task task) {
        taskRepository.save(task);
    }

    public List<Task> getAllTasksByEvent(Event event) {
        return taskRepository.findAllByEventId(event);
    }

    public void delete(Task task) {
        taskRepository.delete(task);
    }
}
