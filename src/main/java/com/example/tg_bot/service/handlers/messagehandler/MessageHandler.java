package com.example.tg_bot.service.handlers.messagehandler;

import com.example.tg_bot.service.handlers.commandshandler.CommandsHandler;
import com.example.tg_bot.service.handlers.menuhandler.MenuHandler;
import com.example.tg_bot.service.handlers.orderpageshandler.OrderPagesHandler;
import com.example.tg_bot.service.handlers.ordershandler.OrderHandler;
import com.example.tg_bot.service.states.BotState;
import com.example.tg_bot.utils.cache.UserData;
import com.example.tg_bot.utils.commands.Commands;
import com.example.tg_bot.utils.text.Language;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.example.tg_bot.utils.commands.Commands.CHECK_ALL_INFO;

@Service
@Component
@Slf4j
@RequiredArgsConstructor
public class MessageHandler {

    private final BotState botState;
    private final UserData userData;
    private final MenuHandler menuHandler;
    private final OrderHandler orderHandler;
    private final OrderPagesHandler orderPagesHandler;
    private final CommandsHandler commandsHandler;

    public SendMessage handleMessage(Message message) {
        String inputMessage = message.getText();
        Long userId = message.getFrom().getId();
        Commands commandNow;

        switch (inputMessage) {
            case "/start", "/choose_language" -> {
                return menuHandler.handleMenu(message);
            }
            case "/buy" -> {
                return handleOrderMessage(message);
            }
            case "English - Англійська" -> {
                userData.saveUsersLanguage(userId, Language.ENG.getLocale());
                return menuHandler.sendMenu(message);
            }
            case "Ukrainian - Українська" -> {
                userData.saveUsersLanguage(userId, Language.UA.getLocale());
                return menuHandler.sendMenu(message);
            }
            default -> commandNow = commandsHandler.message(inputMessage, userId);
        }
        userData.saveUsersCurrentBotState(userId, commandNow);

        return botState.processInputMessage(commandNow, message);
    }

    public SendMessage handleOrderMessage(Message message) {
        Long userId = message.getFrom().getId();
        String item = orderPagesHandler.getCurrentItem();

        userData.saveUsersCurrentBotState(userId, CHECK_ALL_INFO);

        return orderHandler.handleOrderInfo(message, item);
    }
}
