package com.example.tgbot.menu;

import com.example.tgbot.delivery.DeliveryProcessing;
import com.example.tgbot.service.DefaultService;
import com.example.tgbot.user.UserProcessing;
import com.example.tgbot.utils.cache.UserData;
import com.example.tgbot.utils.commands.Commands;
import com.example.tgbot.utils.text.TextSender;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.example.tgbot.utils.sendmessage.Sending.sendMessage;

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
        if (userData.getUsersCurrentBotState(message.getFrom().getId()).equals(Commands.MENU)) {
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
        return Commands.MENU;
    }
}
