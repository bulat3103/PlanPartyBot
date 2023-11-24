package com.example.eventbot.repository;

import com.example.eventbot.model.WishTask;
import com.example.eventbot.model.ids.WishTaskId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishTaskRepository extends JpaRepository<WishTask, WishTaskId> {
}
