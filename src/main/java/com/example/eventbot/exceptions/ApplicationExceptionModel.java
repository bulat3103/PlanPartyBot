package com.example.eventbot.exceptions;

import lombok.Data;

@Data
public class ApplicationExceptionModel {
    private String response;

    private String message;

    public ApplicationExceptionModel(String response, String message) {
        this.response = response;
        this.message = message;
    }
}
