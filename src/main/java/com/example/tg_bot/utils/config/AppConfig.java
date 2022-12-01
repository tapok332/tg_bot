package com.example.tg_bot.utils.config;

import com.example.tg_bot.Bot;
import com.example.tg_bot.service.services.BotService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class AppConfig {

    private BotConfig botConfig;

    @Bean
    public Bot MyBot(BotService botService){
        return new Bot(botConfig, botService);
    }
}
