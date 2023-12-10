package com.example.eventbot.bot;

import com.example.eventbot.model.dto.NotificationDTO;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.jms.*;

@Component
public class NotificationListener implements MessageListener {
    private final MessageExecutor messageExecutor;

    public NotificationListener(MessageExecutor messageExecutor) {
        this.messageExecutor = messageExecutor;
    }

    @Override
    public void onMessage(Message message) {
        try {
            ObjectMessage objectMessage = (ObjectMessage) message;
            NotificationDTO dto = (NotificationDTO) objectMessage.getObject();
            messageExecutor.sendDefaultMessage(SendMessage.builder()
                    .chatId(dto.getUserId())
                    .text(dto.getMessage())
                    .build());
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
