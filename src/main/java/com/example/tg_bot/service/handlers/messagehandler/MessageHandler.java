package com.example.tg_bot.service.handlers.messagehandler;

import com.example.tg_bot.service.BotState;
import com.example.tg_bot.service.handlers.menuhandler.MenuHandler;
import com.example.tg_bot.utils.cache.UserData;
import com.example.tg_bot.utils.commands.Commands;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageHandler {

    private final BotState botState;
    private final UserData userData;
    private final MenuHandler menuHandler;

    public SendMessage handleMessage(Message message) {
        String inputMessage = message.getText();
        Long userId = message.getFrom().getId();
        Commands commandNow;

        switch (inputMessage) {
            case "/start" -> {
                return menuHandler.handleMenu(message);
            }
            case "Go to buy" -> commandNow = Commands.BUY;
            case "My information" -> commandNow = Commands.INFO;
            case "Help" -> commandNow = Commands.HELP;
            default -> commandNow = userData.getUsersCurrentBotState(userId);
        }
        userData.saveUsersCurrentBotState(userId, commandNow);

        return botState.processInputMessage(commandNow, message);
    }
}
