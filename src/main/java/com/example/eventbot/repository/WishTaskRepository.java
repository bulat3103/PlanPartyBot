package com.example.eventbot.repository;

import com.example.eventbot.model.WishTask;
import com.example.eventbot.model.ids.WishTaskId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WishTaskRepository extends JpaRepository<WishTask, WishTaskId> {
    @Query(value = "select * from wish_task where user_id = :userId", nativeQuery = true)
    List<WishTask> getWishTasksByUser(@Param("userId") Long userId);
}
