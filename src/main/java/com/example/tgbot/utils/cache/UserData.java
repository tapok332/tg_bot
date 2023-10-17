package com.example.tgbot.utils.cache;

import com.example.tgbot.delivery.DeliveryDto;
import com.example.tgbot.user.UserDto;
import com.example.tgbot.utils.commands.Commands;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@Component
public class UserData implements DataCache {
    private final Map<Long, Commands> usersBotStates = new HashMap<>();
    private final Map<Long, UserDto> userInfoState = new HashMap<>();
    private final Map<Long, DeliveryDto> userDeliveryInfoState = new HashMap<>();
    private final Map<Long, Locale> userLanguage = new HashMap<>();

    @Override
    public void saveUsersCurrentBotState(Long userId, Commands botState) {
        usersBotStates.put(userId, botState);
    }

    @Override
    public Commands getUsersCurrentBotState(Long userId) {
        if(usersBotStates.get(userId) == null){
            return Commands.MENU;
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

    @Override
    public void saveUsersLanguage(Long userId, Locale language) {
        userLanguage.put(userId, language);
    }

    @Override
    public Locale getUsersLanguage(Long userId) {
        return userLanguage.get(userId);
    }
}
