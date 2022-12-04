package com.example.tg_bot.utils.cache;

import com.example.tg_bot.utils.commands.Commands;
import com.example.tg_bot.entities.dto.DeliveryDto;
import com.example.tg_bot.entities.dto.UserDto;
import com.example.tg_bot.utils.text.Language;

import java.util.Locale;

public interface DataCache {
    void saveUsersCurrentBotState(Long userId, Commands botState);
    Commands getUsersCurrentBotState(Long userId);
    void saveUsersInfoState(Long userId, UserDto userDto);
    UserDto getUsersInfoState(Long userId);
    void saveUsersDeliveryInfoState(Long userId, DeliveryDto deliveryDto);
    DeliveryDto getUsersDeliveryInfoState(Long userId);
    void saveUsersLanguage(Long userId, Locale language);
    Locale getUsersLanguage(Long userId);
}
