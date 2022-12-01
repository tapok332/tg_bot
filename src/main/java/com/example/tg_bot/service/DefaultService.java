package com.example.tg_bot.service;

import com.example.tg_bot.utils.commands.Commands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface DefaultService {
    SendMessage handle(Message message);

    Commands getHandlerName();
}
