package com.example.tgbot.handlers;

import com.example.tgbot.bot.BotState;
import com.example.tgbot.menu.MenuHandler;
import com.example.tgbot.order.OrderHandler;
import com.example.tgbot.order.OrderPagesHandler;
import com.example.tgbot.utils.cache.UserData;
import com.example.tgbot.utils.commands.CommandsHandler;
import com.example.tgbot.utils.text.Language;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.example.tgbot.utils.commands.Commands.CHECK_ALL_INFO;

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
        return switch (message.getText()) {
            case "/start", "/choose_language" -> menuHandler.handleMenu(message);
            case "/buy" -> handleOrderMessage(message);
            case "English - Англійська" -> updateUserLanguage(Language.ENG, message);
            case "Ukrainian - Українська" -> updateUserLanguage(Language.UA, message);
            default -> checkAndHandleNextState(message);
        };
    }

    private SendMessage checkAndHandleNextState(Message message) {
        var userId = message.getFrom().getId();

        var commandNow = commandsHandler.message(message.getText(), userId);
        userData.saveUsersCurrentBotState(userId, commandNow);

        return botState.processInputMessage(commandNow, message);
    }

    private SendMessage updateUserLanguage(Language eng, Message message) {
        userData.saveUsersLanguage(message.getFrom().getId(), eng.getLocale());
        return menuHandler.sendMenu(message);
    }

    public SendMessage handleOrderMessage(Message message) {
        String item = orderPagesHandler.getCurrentItem();

        userData.saveUsersCurrentBotState(message.getFrom().getId(), CHECK_ALL_INFO);

        return orderHandler.handleOrderInfo(message, item);
    }
}
