package com.example.tg_bot;

import com.example.tg_bot.utils.config.BotConfig;
import com.example.tg_bot.service.services.BotService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class Bot extends TelegramLongPollingBot {

    private final BotConfig config;
    private final BotService botService;

    @SneakyThrows
    public Bot(BotConfig config, BotService botService) {
        this.config = config;
        this.botService = botService;
    }

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage handledMessage = botService.handleUpdate(update);
        sendMessageToChat(handledMessage);
    }

    @SneakyThrows
    public void sendMessageToChat(SendMessage message) {
        new TelegramLongPollingBot().executeAsync()
        execute(message);
    }
}
