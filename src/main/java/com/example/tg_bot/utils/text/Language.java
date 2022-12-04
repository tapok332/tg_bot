package com.example.tg_bot.utils.text;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

@Getter
@AllArgsConstructor
public enum Language {
    ENG(new Locale("en", "EN")),
    UA(new Locale("ua", "UA"));

    private final Locale locale;
}
