package com.example.tg_bot.service;

import com.example.tg_bot.utils.commands.Commands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.example.tg_bot.utils.text.en.TextsForMessage.INCORRECT_COMMAND;
import static com.example.tg_bot.utils.utilforsendmessage.Sending.sendMessage;

public abstract class DefaultState {

    public SendMessage processInputMessage(Commands currentState, Message message){
        DefaultService currentMessageHandler = findMessageHandler(currentState);
        if (currentMessageHandler == null) {
            return sendMessage(INCORRECT_COMMAND.getText(), message.getChatId());
        }
        return currentMessageHandler.handle(message);
    }
    public abstract DefaultService findMessageHandler(Commands currentState);
}
