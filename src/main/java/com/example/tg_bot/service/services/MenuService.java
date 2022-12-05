package com.example.tg_bot.service.services;

import com.example.tg_bot.service.DefaultService;
import com.example.tg_bot.utils.text.TextSender;
import com.example.tg_bot.service.handlers.menuhandler.MenuHandler;
import com.example.tg_bot.utils.cache.UserData;
import com.example.tg_bot.utils.commands.Commands;
import com.example.tg_bot.service.handlers.deliveryhandler.processing.DeliveryProcessing;
import com.example.tg_bot.service.handlers.userhandler.processing.UserProcessing;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.example.tg_bot.utils.sendmessage.Sending.sendMessage;

@Service
@Component
@RequiredArgsConstructor
public class MenuService implements DefaultService {

    private final UserData userData;
    private final UserProcessing userProcessing;
    private final DeliveryProcessing deliveryProcessing;
    private final TextSender textSender;
    private final MenuHandler menuHandler;

    @Override
    public SendMessage execute(Message message) {
        if (userData.getUsersCurrentBotState(message.getFrom().getId()).equals(Commands.TEXT_PROCESSING)) {
            return menuHandler.sendMenu(message);
        }
        return processingMessage(message);
    }

    private SendMessage processingMessage(Message message) {
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();
        Commands botState = userData.getUsersCurrentBotState(message.getFrom().getId());
        switch (botState) {
            case CHECK_USER_INFO -> {
                return getAllInfo(userId, chatId);
            }
            case HELP -> {
                return sendMessage(textSender.getText(userId, "help_message"), chatId);
            }
            default -> {
                return sendMessage(textSender.getText(userId, "error_unknown"), chatId);
            }
        }
    }

    private SendMessage getAllInfo(Long userId, Long chatId) {
        return sendMessage(userProcessing.checkInfo(userId) + "\n\n" + deliveryProcessing.checkInfo(userId), chatId);
    }

    @Override
    public Commands getExecuteName() {
        return Commands.TEXT_PROCESSING;
    }
}
