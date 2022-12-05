package com.example.tg_bot.utils.text;

import com.example.tg_bot.utils.cache.UserData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class TextSender {
    private final UserData userData;

    public String getText(Long userId, String propertyKey) {
        Locale userLanguage = userData.getUsersLanguage(userId);
        ResourceBundle bundle;
        if (userLanguage == null) {
            bundle = ResourceBundle.getBundle("texts.text", Locale.ENGLISH);
        } else {
            bundle = ResourceBundle.getBundle("texts.text", userLanguage);
        }
        return bundle.getString(propertyKey);
    }
}
