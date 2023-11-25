package com.example.eventbot.services;

import com.example.eventbot.bot.MessageExecutor;
import com.example.eventbot.exceptions.ExceptionDescriptor;
import com.example.eventbot.model.enums.NotificationLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActionService {
    private final UserService userService;
    private final ChatService chatService;
    private final MessageExecutor messageExecutor;

    public void joinChatByCode(Message message) throws TelegramApiException {
        log.info("invoke joinChatByCode: ({}, {})", message.getChatId(), message.getFrom().getUserName());
        ExceptionDescriptor.NO_INFO_ERROR.throwIfFalse(userService.checkUser(message.getFrom().getId()));
        ExceptionDescriptor.JOIN_ERROR.throwIfFalse(chatService.checkCode(Integer.valueOf(message.getText().trim())));
        String answer = "Спасибо за присоединение!";

        messageExecutor.sendDefaultMessage(SendMessage.builder()
                .chatId(message.getChatId())
                .text(answer)
                .build());
    }

    public void setNotificationLevel(Update update, String callBackData) throws TelegramApiException {
        log.info("invoke setNotificationLevel: ({}, {})", update.getCallbackQuery().getFrom().getId(), callBackData);
        Long userTelegramId = update.getCallbackQuery().getFrom().getId();
        ExceptionDescriptor.NO_INFO_ERROR.throwIfFalse(userService.checkUser(userTelegramId));
        NotificationLevel newLevel = NotificationLevel.valueOf(callBackData);
        String answer = "Настройки уведомлений изменены";
        Long chatTelegramId = update.getCallbackQuery().getMessage().getChatId();

        EditMessageText message = new EditMessageText();
        message.setChatId(userTelegramId);
        message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        message.setText(answer);

        messageExecutor.editMessage(message);
    }
}
