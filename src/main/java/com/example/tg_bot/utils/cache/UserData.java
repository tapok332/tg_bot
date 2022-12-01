package com.example.tg_bot.utils.cache;

import com.example.tg_bot.utils.commands.Commands;
import com.example.tg_bot.entities.dto.DeliveryDto;
import com.example.tg_bot.entities.dto.UserDto;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Service
@Component
public class UserData implements DataCache {
    private final Map<Long, Commands> usersBotStates = new HashMap<>();
    private final Map<Long, UserDto> userInfoState = new HashMap<>();
    private final Map<Long, DeliveryDto> userDeliveryInfoState = new HashMap<>();

    @Override
    public void saveUsersCurrentBotState(Long userId, Commands botState) {
        usersBotStates.put(userId, botState);
    }

    @Override
    public Commands getUsersCurrentBotState(Long userId) {
        if(usersBotStates.get(userId) == null){
            return Commands.NONE;
        }
        return usersBotStates.get(userId);
    }

    @Override
    public void saveUsersInfoState(Long userId, UserDto userDto) {
        userInfoState.put(userId, userDto);
    }

    @Override
    public UserDto getUsersInfoState(Long userId) {
        return userInfoState.get(userId);
    }

    @Override
    public void saveUsersDeliveryInfoState(Long userId, DeliveryDto deliveryDto) {
        userDeliveryInfoState.put(userId, deliveryDto);
    }

    @Override
    public DeliveryDto getUsersDeliveryInfoState(Long userId) {
        return userDeliveryInfoState.get(userId);
    }
}
