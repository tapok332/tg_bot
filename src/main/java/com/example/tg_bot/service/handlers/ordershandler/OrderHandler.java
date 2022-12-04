package com.example.tg_bot.service.handlers.ordershandler;

import com.example.tg_bot.service.handlers.informationhandler.InfoHandler;
import com.example.tg_bot.service.handlers.languagehandler.LanguageHandler;
import com.example.tg_bot.service.handlers.ordershandler.orderprocessing.OrderProcessing;
import com.example.tg_bot.service.states.OrderState;
import com.example.tg_bot.utils.cache.UserData;
import com.example.tg_bot.utils.commands.Commands;
import com.example.tg_bot.validation.UserValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.example.tg_bot.utils.text.en.TextsForMessage.ORDERS_INFO;
import static com.example.tg_bot.utils.utilforsendmessage.Sending.sendMessage;
import static com.example.tg_bot.utils.utilforsendmessage.Sending.sendMessageWithButton;

@Component
@RequiredArgsConstructor
public class OrderHandler {

    private final OrderState orderState;
    private final UserData userData;
    private final UserValidate userValidate;
    private final InfoHandler infoHandler;
    private final OrderProcessing orderProcessing;
    private final LanguageHandler languageHandler;

    public SendMessage handleOrder(Message message) {
        String inputMessage = message.getText();
        Long userId = message
                .getFrom()
                .getId();
        Commands commandNow = userData.getUsersCurrentBotState(userId);

        if (inputMessage.equals(languageHandler.getText(userId, "go_buy"))) {
            return sendMessageWithButton(ORDERS_INFO.getText(), message.getChatId(), getOrderButtons(userId));
        }
        if (inputMessage.equals(languageHandler.getText(userId, "menu"))) {
            commandNow = Commands.TEXT_PROCESSING;
        }

        return orderState.processInputMessage(commandNow, message);
    }

    public SendMessage handleOrderInfo(Message message, String item) {
        return validateUser(message, item);
    }

    private SendMessage validateUser(Message message, String item) {
        Long userId = message.getFrom().getId();
        if (!userValidate.hasUser(userId) || !userValidate.hasUserAddress(userId)) {
            message.setText("without info");

            return infoHandler.handleInfo(message);
        }
        if (userValidate.hasUser(userId) && userValidate.hasUserAddress(userId)) {
            orderProcessing.getOrderInfo(item.substring(12, 13));
            message.setText(languageHandler.getText(userId, "check_info"));

            String userInfo = infoHandler.handleInfo(message).getText();

            return sendMessage(item + "\n\n" + userInfo, message.getChatId());
        }
        return sendMessage(languageHandler.getText(userId, "error_validate"), message.getChatId());
    }

    private List<List<InlineKeyboardButton>> getOrderButtons(Long userId) {
        InlineKeyboardButton laptopsButton = InlineKeyboardButton.builder()
                .text(languageHandler.getText(userId, "laptops"))
                .callbackData("laptops")
                .build();
        InlineKeyboardButton keyboardsButton = InlineKeyboardButton.builder()
                .text(languageHandler.getText(userId, "keyboards"))
                .callbackData("keyboards")
                .build();
        List<InlineKeyboardButton> keyboard = List.of(laptopsButton, keyboardsButton);

        return List.of(keyboard);
    }
}
