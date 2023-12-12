package com.example.eventbot.model.enums;

public enum NotificationLevel {
    DAY("раз в день", 1),
    TWO_DAY("раз в 2 дня", 2),
    THREE_DAY("раз в 3 дня", 3),
    WEEK("раз в неделю", 7);

    private String desc;
    private int days;

    NotificationLevel(String desc, int days) {
        this.desc = desc;
        this.days = days;
    }

    public String getDesc() {
        return desc;
    }

    public int getDays() {
        return days;
    }
}
