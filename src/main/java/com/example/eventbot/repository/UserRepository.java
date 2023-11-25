package com.example.eventbot.repository;

import com.example.eventbot.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserEntityByTelegramId(Long telegramId);

    Optional<UserEntity> findUserEntityByUserTag(String userTag);
}
