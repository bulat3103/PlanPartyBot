package com.example.eventbot.services;

import com.example.eventbot.model.Event;
import com.example.eventbot.model.Task;
import com.example.eventbot.model.UserEntity;
import com.example.eventbot.model.WishTask;
import com.example.eventbot.model.ids.WishTaskId;
import com.example.eventbot.repository.WishTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishTaskService {
    private final WishTaskRepository wishTaskRepository;

    public void create(Task task, UserEntity user, int ordinal) {
        WishTask wishTask = new WishTask();
        WishTaskId id = new WishTaskId(task, user);
        wishTask.setId(id);
        wishTask.setOrdinal(ordinal);
        wishTaskRepository.save(wishTask);
    }

    public List<WishTask> getTasksByUser(UserEntity user, Event event) {
        return wishTaskRepository.getWishTasksByUser(user.getId()).stream().filter(t -> t.getId().getTaskId().getEventId().equals(event)).collect(Collectors.toList());
    }
}
