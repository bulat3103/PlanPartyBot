package com.example.eventbot.services.commands.impl;

import com.example.eventbot.exceptions.ApplicationException;
import com.example.eventbot.model.dto.ChatDto;
import com.example.eventbot.model.dto.UserDto;
import com.example.eventbot.services.ChatService;
import com.example.eventbot.services.UserService;
import com.example.eventbot.services.commands.CommandStrategy;
import com.example.eventbot.utils.Answers;
import com.example.eventbot.utils.Command;
import com.example.eventbot.utils.MapStructMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Service
@RequiredArgsConstructor
public class StartCommandStrategy implements CommandStrategy {

    private final UserService userService;
    private final ChatService chatService;
    private final MapStructMapper mapper;

    @Override
    public SendMessage invokeCommand(Message message) throws ApplicationException {
        log.info("invoke command START: ({}, {})", message.getChatId(), message.getFrom().getUserName());
        String answer;
        if (message.getChat().getType().equals("private")) {
            if (!userService.checkUser(message.getFrom().getId())) {
                UserDto userDto = mapper.mapToUserDto(message.getFrom());
                userService.create(userDto);
                answer = Answers.START_IN_CHAT;
            } else {
                answer = "Вы уже привязаны к чату";
            }
        } else {
            answer = Answers.START_IN_CHAT;
            if (chatService.checkChat(message.getChatId())) {
                answer += chatService.getChat(message.getChatId()).getCode();
            } else {
                ChatDto dto = new ChatDto();
                dto.setTelegramId(message.getChatId());
                dto.setCode(chatService.generateCode());
                chatService.create(dto);
                answer += dto.getCode();
            }
        }
        return SendMessage
                .builder()
                .chatId(message.getChatId())
                .text(answer)
                .build();
    }

    @Override
    public Command getSupportedCommand() {
        return Command.START;
    }
}
