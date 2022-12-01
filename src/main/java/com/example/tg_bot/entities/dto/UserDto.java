package com.example.tg_bot.entities.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Data
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    private String name;
    private String surname;
    private String patronymic;
    private Integer age;
}
