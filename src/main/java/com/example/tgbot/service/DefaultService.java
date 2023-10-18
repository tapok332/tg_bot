package com.example.tgbot.service;

import com.example.tgbot.utils.commands.Commands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface DefaultService {
    SendMessage execute(Message message);

    Commands getExecuteName();
}
