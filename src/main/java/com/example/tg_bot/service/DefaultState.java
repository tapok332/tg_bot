package com.example.tg_bot.service;

import com.example.tg_bot.utils.commands.Commands;
import com.example.tg_bot.utils.text.TextSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.example.tg_bot.utils.sendmessage.Sending.sendMessage;

@Component
public abstract class DefaultState {
    @Autowired
    private TextSender textSender;

    public SendMessage processInputMessage(Commands currentState, Message message) {
        DefaultService currentMessageHandler = findMessageHandler(currentState);
        if (currentMessageHandler == null) {
            return sendMessage(textSender.getText(message.getFrom().getId(), "error_unknown"), message.getChatId());
        }
        return currentMessageHandler.execute(message);
    }

    public abstract DefaultService findMessageHandler(Commands currentState);
}
