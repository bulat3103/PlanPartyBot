package com.example.eventbot.utils;

import com.example.eventbot.exceptions.ApplicationException;
import com.example.eventbot.exceptions.ExceptionDescriptor;
import com.example.eventbot.model.enums.NotificationLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Slf4j
@Component
public class MessageParser {

    public Command checkCommand(String text) throws ApplicationException {
        String command = text.split("/")[1];
        if (command.contains(" ")) {
            command = command.split(" ")[0];
        }
        try {
            return Command.valueOf(command.toUpperCase().trim());
        } catch (Exception e) {
            log.info("Invalid command {}", command);
            throw ExceptionDescriptor.INVALID_COMMAND.exception();
        }
    }

    public Boolean hasCommand(String text) {
        return !ObjectUtils.isEmpty(text) && text.charAt(0) == '/';
    }

    public Boolean hasCode(String text) {
        if (text.length() != 4) return false;
        String regEx = "\\d*";
        return text.trim().matches(regEx);
    }

    public Boolean hasDate(String text) {
        String regEx = "[0-9]{2}.[0-9]{2}.[0-9]{4}";
        return text.trim().matches(regEx);
    }

    public Boolean checkForNotification(String text) throws ApplicationException {
        try {
            NotificationLevel.valueOf(text.toUpperCase().trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
