package com.example.eventbot.services;

import com.example.eventbot.exceptions.ApplicationException;
import com.example.eventbot.exceptions.ExceptionDescriptor;
import com.example.eventbot.model.ChatEntity;
import com.example.eventbot.model.ChatUser;
import com.example.eventbot.model.UserEntity;
import com.example.eventbot.model.dto.UserDto;
import com.example.eventbot.repository.ChatRepository;
import com.example.eventbot.repository.UserRepository;
import com.example.eventbot.utils.MapStructMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MapStructMapper mapper;

    public void create(UserDto dto) {
        UserEntity userEntity = mapper.mapToUserEntity(dto);
        userRepository.save(userEntity);
    }

    public Boolean checkUser(Long telegramId) {
        return userRepository.findUserEntityByTelegramId(telegramId).isPresent();
    }

    public Boolean checkByTag(String userTag) {
        return userRepository.findUserEntityByUserTag(userTag).isPresent();
    }

    @Transactional
    public void addUserToChat(Long telegramId, Integer code) throws ApplicationException {
        UserEntity user = userRepository.findUserEntityByTelegramId(telegramId)
                .orElseThrow(ExceptionDescriptor.APPLICATION_ERROR::exception);
        ChatEntity chat = chatRepository.findChatEntityByCode(code)
                .orElseThrow(ExceptionDescriptor.APPLICATION_ERROR::exception);

        Set<ChatUser> users = chat.getChatUser();
        if (users.stream().noneMatch(item -> item.getUser().equals(user))) {
            ChatUser chatUser = new ChatUser();
            chatUser.setUser(user);
            chatUser.setChat(chat);
            users.add(chatUser);
            chat.setChatUser(users);
            chatRepository.save(chat);
        }
    }
}
