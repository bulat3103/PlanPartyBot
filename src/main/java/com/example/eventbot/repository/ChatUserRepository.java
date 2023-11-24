package com.example.eventbot.repository;

import com.example.eventbot.model.ChatUser;
import com.example.eventbot.model.ids.ChatUsersId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatUserRepository extends JpaRepository<ChatUser, ChatUsersId> {
}
