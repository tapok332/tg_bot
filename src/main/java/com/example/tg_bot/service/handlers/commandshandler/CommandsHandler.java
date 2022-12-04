package com.example.tg_bot.service.handlers.commandshandler;

import com.example.tg_bot.service.handlers.languagehandler.LanguageHandler;
import com.example.tg_bot.utils.cache.UserData;
import com.example.tg_bot.utils.commands.Commands;
import com.example.tg_bot.utils.text.Language;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static com.example.tg_bot.utils.text.en.TextsForMessage.INFO;
import static com.example.tg_bot.utils.text.en.TextsForMessage.WITHOUT_INFO;
import static com.example.tg_bot.utils.utilforsendmessage.Sending.sendMessage;
import static com.example.tg_bot.utils.utilforsendmessage.Sending.sendMessageWithKeyboard;

@Component
@RequiredArgsConstructor
public class CommandsHandler {

    private final UserData userData;
     private final LanguageHandler languageHandler;

    public SendMessage info(Message message, ReplyKeyboardMarkup keyboardMarkup){
        if(message.getText().equals(languageHandler.getText(message.getFrom().getId(), "my_info"))){
            return sendMessageWithKeyboard(INFO.getText(), message.getChatId(), keyboardMarkup);
        }
        if(message.getText().equals("without info")){
            return sendMessageWithKeyboard(WITHOUT_INFO.getText(), message.getChatId(), keyboardMarkup);
        }
        return sendMessage(languageHandler.getText(message.getFrom().getId(), "error_info_handle"), message.getChatId());
    }

    public Commands info(Message message){
        String inputMessage = message.getText();
        Long userId = message
                .getFrom()
                .getId();

        if(inputMessage.equals(languageHandler.getText(userId,"user_info"))){
            return Commands.USER_INFO;
        }
        if(inputMessage.equals(languageHandler.getText(userId,"user_delivery_info"))){
            return Commands.DELIVERY_INFO;
        }
        if(inputMessage.equals(languageHandler.getText(userId,"check_info"))){
            return Commands.CHECK_USER_INFO;
        }
        if(inputMessage.equals(languageHandler.getText(userId,"delete_info"))){
            return Commands.DELETE_USER_INFO;
        }

        return getMenuOrHelp(inputMessage, userId);
    }

    public Commands message(String inputMessage, Long userId){
        if(inputMessage.equals(languageHandler.getText(userId,"go_buy"))){
            return Commands.BUY;
        }
        if(inputMessage.equals(languageHandler.getText(userId,"my_info"))){
            return Commands.INFO;
        }

        return getMenuOrHelp(inputMessage, userId);
    }

    private Commands getMenuOrHelp(String inputMessage, Long userId){
        if(inputMessage.equals(languageHandler.getText(userId,"menu"))){
            return Commands.TEXT_PROCESSING;
        }
        if(inputMessage.equals(languageHandler.getText(userId,"help"))){
            return Commands.HELP;
        }
        return userData.getUsersCurrentBotState(userId);
    }
}
