package com.example.eventbot.repository;

import com.example.eventbot.model.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
    Optional<ChatEntity> findChatEntityByTelegramId(Long telegramId);

    Optional<ChatEntity> findChatEntityByCode(Integer code);
}
