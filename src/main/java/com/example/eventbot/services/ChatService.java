package com.example.eventbot.services;

import com.example.eventbot.configs.BotConfig;
import com.example.eventbot.exceptions.ApplicationException;
import com.example.eventbot.exceptions.ExceptionDescriptor;
import com.example.eventbot.model.ChatEntity;
import com.example.eventbot.model.dto.ChatDto;
import com.example.eventbot.repository.ChatRepository;
import com.example.eventbot.utils.MapStructMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final BotConfig botConfig;

    public Boolean checkChat(Long telegramId) {
        return chatRepository.findChatEntityByTelegramId(telegramId).isPresent();
    }

    public Boolean checkCode(Integer code) {
        return chatRepository.findChatEntityByCode(code).isPresent();
    }

    public void create(ChatDto dto) {
        ChatEntity chat = new ChatEntity();
        chat.setTelegramId(dto.getTelegramId());
        chat.setCode(dto.getCode());
        chatRepository.save(chat);
    }

    public ChatEntity getChat(Long telegramId) throws ApplicationException {
        return chatRepository.findChatEntityByTelegramId(telegramId).orElseThrow(ExceptionDescriptor.APPLICATION_ERROR::exception);
    }

    public ChatEntity getChatByCode(Integer code) throws ApplicationException {
        return chatRepository.findChatEntityByCode(code).orElseThrow(ExceptionDescriptor.APPLICATION_ERROR::exception);
    }

    public Integer generateCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < botConfig.getChatCodeSize(); i++) {
            int digit = new Random().nextInt(10);
            while (digit == 0 && i == 0) digit = new Random().nextInt(10);
            code.append(digit);
        }
        Integer newCode = Integer.parseInt(code.toString());
        return checkCode(newCode) ? generateCode() : newCode;
    }

    public void delete(Long telegramId) throws ApplicationException {
        ChatEntity chat = chatRepository.findChatEntityByTelegramId(telegramId).orElseThrow(ExceptionDescriptor.APPLICATION_ERROR::exception);
        chatRepository.delete(chat);
    }
}
