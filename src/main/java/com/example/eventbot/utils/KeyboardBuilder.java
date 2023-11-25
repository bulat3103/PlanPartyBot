package com.example.eventbot.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class KeyboardBuilder {

    public InlineKeyboardMarkup getButtonList(Map<String, String> buttons) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        for (Map.Entry<String, String> entry : buttons.entrySet()) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            String key = entry.getKey();
            String value = entry.getValue();
            rowInline.add(getButton(key, value));
            rowsInline.add(rowInline);
        }
        markup.setKeyboard(rowsInline);
        return markup;
    }

    public InlineKeyboardButton getButton(String text, String callBack) {
        InlineKeyboardButton newButton = new InlineKeyboardButton();
        newButton.setText(text);
        newButton.setCallbackData(callBack);
        return newButton;
    }
}
