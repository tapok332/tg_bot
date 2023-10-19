package com.example.tgbot.bot;

import com.example.tgbot.utils.secrets.SecretService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${bot.token}")
    private String localToken;
    private final SecretService secretService;

    String getBotToken() {
        if ("default".equals(System.getProperty("spring.profiles.active", "default"))) {
            return new JSONObject(secretService.getBotToken()).getString("botToken");
        }
        return localToken;
    }
}
