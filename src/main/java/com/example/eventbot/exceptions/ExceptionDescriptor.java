package com.example.eventbot.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;

@Getter
@RequiredArgsConstructor
public enum ExceptionDescriptor {
    INVALID_COMMAND("Команда не найдена", "Invalid command"),
    HANDLER_NOT_FOUND("Что-то пошло не так", "Handler not found"),
    NO_INFO_ERROR("К сожалению, мы еще не знакомы, воспользуйтесь командой /start, чтобы начать пользоваться ботом", "User not registered"),
    JOIN_ERROR("Неверный код, введите четырехзначный код из чата", "Invalid join data");

    private final String responseText;

    private final String message;

    public void throwException() throws ApplicationException {
        throw exception();
    }

    public void throwIfNullOrEmpty(Object obj) throws ApplicationException {
        if (ObjectUtils.isEmpty(obj)) throw exception();
    }

    public void throwIfTrue(Boolean condition) throws ApplicationException {
        if (condition) throw exception();
    }

    public void throwIfFalse(Boolean condition) throws ApplicationException {
        if (!condition) throw exception();
    }

    public ApplicationExceptionModel applicationExceptionModel() {
        return new ApplicationExceptionModel(this.responseText, this.message);
    }

    public ApplicationException exception() {
        return new ApplicationException(applicationExceptionModel());
    }
}
