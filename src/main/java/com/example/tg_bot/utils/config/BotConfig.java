package com.example.tg_bot.utils.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
@PropertySource("application.yaml")
public class BotConfig {

    @Value("${bot.token}")
    public String token;

    @Value("${bot.name}")
    public String name;

    public final String username = "@compshop_zks21_bot";
}
