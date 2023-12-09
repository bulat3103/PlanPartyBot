package com.example.eventbot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventDto {
    public Integer chatCode;
    public String date;
    public String name;
    public String listOfWork;
}
