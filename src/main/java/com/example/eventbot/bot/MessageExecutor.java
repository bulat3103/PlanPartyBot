package com.example.eventbot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class MessageExecutor {
    private final @Lazy Bot bot;

    public MessageExecutor(@Lazy Bot bot) {
        this.bot = bot;
    }

    public Message sendDefaultMessage(String text, Message message) throws TelegramApiException {
        if (!ObjectUtils.isEmpty(text)) {
            return bot.execute(SendMessage.builder()
                    .chatId(message.getChatId())
                    .text(text)
                    .build());
        }
        return null;
    }

    public void sendDefaultMessage(SendMessage sendMessage) throws TelegramApiException {
        if (!ObjectUtils.isEmpty(sendMessage) && !ObjectUtils.isEmpty(sendMessage.getText())) {
            bot.execute(sendMessage);
        }
    }

    public void sendDefaultAndDeletePrevious(SendMessage sendMessage, Message previous) throws TelegramApiException {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(previous.getChatId());
        deleteMessage.setMessageId(previous.getMessageId());
        bot.execute(deleteMessage);
        if (!ObjectUtils.isEmpty(sendMessage) && !ObjectUtils.isEmpty(sendMessage.getText())) {
            bot.execute(sendMessage);
        }
    }

    public void editMessage(EditMessageText editMessageText) throws TelegramApiException {
        if (!ObjectUtils.isEmpty(editMessageText)) {
            bot.execute(editMessageText);
        }
    }
}
