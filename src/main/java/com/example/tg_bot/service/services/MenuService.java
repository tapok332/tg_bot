package com.example.tg_bot.service.services;

import com.example.tg_bot.service.DefaultService;
import com.example.tg_bot.utils.cache.UserData;
import com.example.tg_bot.utils.commands.Commands;
import com.example.tg_bot.service.handlers.deliveryhandler.processing.DeliveryProcessing;
import com.example.tg_bot.service.handlers.userhandler.processing.UserProcessing;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.example.tg_bot.utils.utilforsendmessage.Sending.sendMessage;
import static com.example.tg_bot.utils.text.en.TextsForMessage.*;

@Component
@Service
@AllArgsConstructor
public class MenuService implements DefaultService {

    private final UserData userData;
    private final UserProcessing userProcessing;
    private final DeliveryProcessing deliveryProcessing;

    @Override
    public SendMessage handle(Message message) {
        if (userData.getUsersCurrentBotState(message.getFrom().getId()).equals(Commands.TEXT_PROCESSING)) {
            return sendMessage(MENU.getText(), message.getChatId());
        }
        return processingMessage(message);
    }

    private SendMessage processingMessage(Message message) {
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();
        Commands botState = userData.getUsersCurrentBotState(message.getFrom().getId());
        switch (botState) {
            case CHECK_INFO -> {
                return getAllInfo(userId, chatId);
            }
            case HELP -> {
                return sendMessage(HELP.getText(), chatId);
            }
            default -> {
                return sendMessage(UNKNOWN_ERROR.getText(), chatId);
            }
        }
    }

    private SendMessage getAllInfo(Long userId, Long chatId) {
        return sendMessage(userProcessing.checkInfo(userId) + "\n\n" + deliveryProcessing.checkInfo(userId), chatId);
    }

    @Override
    public Commands getHandlerName() {
        return Commands.TEXT_PROCESSING;
    }
}
