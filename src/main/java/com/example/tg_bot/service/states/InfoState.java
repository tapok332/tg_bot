package com.example.tg_bot.service.states;

import com.example.tg_bot.service.DefaultService;
import com.example.tg_bot.service.DefaultState;
import com.example.tg_bot.utils.commands.Commands;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InfoState extends DefaultState {

    private final Map<Commands, DefaultService> services = new HashMap<>();

    public InfoState(List<DefaultService> services) {
        services.forEach(handler -> this.services.put(handler.getHandlerName(), handler));
    }

    @Override
    public DefaultService findMessageHandler(Commands currentState) {
        switch (currentState) {
            case USER_INFO, SET_USER_INFO, UPDATE_USER_INFO -> {
                return services.get(Commands.USER_INFO);
            }
            case DELIVERY_INFO, SET_DELIVERY_INFO, UPDATE_DELIVERY_INFO -> {
                return services.get(Commands.DELIVERY_INFO);
            }
            case CHECK_USER_INFO, DELETE_USER_INFO -> {
                return services.get(Commands.ALL_INFO);
            }
            case CHECK_ALL_INFO -> {
                return services.get(Commands.USER_AND_ORDER_INFO);
            }
            default -> {
                return services.get(currentState);
            }
        }
    }
}
