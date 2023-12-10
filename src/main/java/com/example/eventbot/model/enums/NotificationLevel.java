package com.example.eventbot.model.enums;

public enum NotificationLevel {
    DAY("раз в день"),
    TWO_DAY("раз в 2 дня"),
    THREE_DAY("раз в 3 дня"),
    WEEK("раз в неделю");

    private String desc;

    NotificationLevel(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
