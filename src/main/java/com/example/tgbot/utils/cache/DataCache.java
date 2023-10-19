package com.example.tgbot.utils.cache;

import com.example.tgbot.utils.commands.Commands;

import java.util.Locale;

public interface DataCache {
    void saveUsersCurrentBotState(Long userId, Commands botState);
    Commands getUsersCurrentBotState(Long userId);
    void saveUsersLanguage(Long userId, Locale language);
    Locale getUsersLanguage(Long userId);
}
