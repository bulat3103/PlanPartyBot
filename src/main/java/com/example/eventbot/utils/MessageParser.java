package com.example.eventbot.utils;

import com.example.eventbot.exceptions.ApplicationException;
import com.example.eventbot.exceptions.ExceptionDescriptor;
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
        String regEx = "\\d*";
        return text.trim().matches(regEx);
    }

    public Boolean hasUserTag(String text) {
        return text.charAt(0) == '@';
    }
}
