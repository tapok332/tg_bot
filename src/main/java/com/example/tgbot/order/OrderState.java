package com.example.tgbot.order;

import com.example.tgbot.service.DefaultService;
import com.example.tgbot.service.DefaultState;
import com.example.tgbot.utils.commands.Commands;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderState extends DefaultState {

    private final Map<Commands, DefaultService> services = new HashMap<>();

    public OrderState(List<DefaultService> services) {
        services.forEach(handler -> this.services.put(handler.getExecuteName(), handler));
    }

    @Override
    public DefaultService findMessageHandler(Commands currentState) {
        switch (currentState) {
            case KEYBOARD_ORDERS -> {
                return services.get(Commands.KEYBOARD_ORDERS);
            }
            case LAPTOP_ORDERS -> {
                return services.get(Commands.LAPTOP_ORDERS);
            }
            default -> {
                return services.get(currentState);
            }
        }
    }
}
