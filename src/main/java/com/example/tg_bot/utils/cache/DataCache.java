package com.example.tg_bot.utils.cache;

import com.example.tg_bot.utils.commands.Commands;
import com.example.tg_bot.entities.dto.DeliveryDto;
import com.example.tg_bot.entities.dto.UserDto;

public interface DataCache {
    void saveUsersCurrentBotState(Long userId, Commands botState);
    Commands getUsersCurrentBotState(Long userId);
    void saveUsersInfoState(Long userId, UserDto userDto);
    UserDto getUsersInfoState(Long userId);
    void saveUsersDeliveryInfoState(Long userId, DeliveryDto deliveryDto);
    DeliveryDto getUsersDeliveryInfoState(Long userId);
}
