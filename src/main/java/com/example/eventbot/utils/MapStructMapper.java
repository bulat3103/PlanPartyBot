package com.example.eventbot.utils;

import com.example.eventbot.model.ChatEntity;
import com.example.eventbot.model.UserEntity;
import com.example.eventbot.model.dto.ChatDto;
import com.example.eventbot.model.dto.UserDto;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
public class MapStructMapper {
    public UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setTelegramId(user.getId());
        userDto.setUserTag(user.getUserName());
        userDto.setName(user.getFirstName());
        userDto.setSurname(user.getLastName());
        return userDto;
    }

    public UserEntity mapToUserEntity(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setTelegramId(userDto.getTelegramId());
        userEntity.setUserTag(userDto.getUserTag());
        userEntity.setName(userDto.getName());
        userEntity.setSurname(userDto.getSurname());
        return userEntity;
    }

    public ChatDto mapToChatDto(ChatEntity chat) {
        ChatDto dto = new ChatDto();
        dto.setTelegramId(chat.getTelegramId());
        dto.setCode(chat.getCode());
        return dto;
    }
}
