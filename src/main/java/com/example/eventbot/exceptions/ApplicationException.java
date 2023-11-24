package com.example.eventbot.exceptions;

import lombok.Getter;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Getter
public class ApplicationException extends TelegramApiException {
    private final ApplicationExceptionModel exception;

    public ApplicationException(ApplicationExceptionModel exception) {
        super(exception.getMessage());
        this.exception = exception;
    }

    public ApplicationException(ApplicationExceptionModel exception, Throwable cause) {
        super(exception.getMessage(), cause);
        this.exception = exception;
    }
}
