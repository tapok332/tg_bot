package com.example.tgbot.utils.cache;

import com.example.tgbot.delivery.DeliveryDto;
import com.example.tgbot.user.UserDto;
import com.example.tgbot.utils.commands.Commands;

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
