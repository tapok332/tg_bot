package com.example.tg_bot.validation;

import com.example.tg_bot.repo.UserRepository;
import com.example.tg_bot.utils.cache.UserData;
import com.example.tg_bot.utils.commands.Commands;
import com.example.tg_bot.utils.text.TextSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.example.tg_bot.utils.sendmessage.Sending.sendMessage;

@Component
@Slf4j
@AllArgsConstructor
public class UserValidate {

    private final UserRepository userRepository;
    private final UserData userData;
    private final TextSender textSender;

    public boolean hasUser(Long userId) {
        return userRepository.findByUserId(userId).isPresent();
    }

    public boolean hasUserAddress(Long userId) {
        return userRepository.findByUserId(userId).isPresent() && userRepository.findByUserId(userId).get().getAddress() != null;
    }

    public SendMessage setUserInfo(Long userId, Long chatId) {
        log.info("Validate for set user information for user = {} by chat = {}", userId, chatId);
        if (this.hasUser(userId)) {
            return sendMessage(textSender.getText(userId, "error_user_already_register"), chatId);
        } else {
            userData.saveUsersCurrentBotState(userId, Commands.SET_USER_INFO);
            return sendMessage(textSender.getText(userId, "send_name"), chatId);
        }
    }

    public SendMessage setUserAddress(Long userId, Long chatId) {
        log.info("Validate for set user address information for user = {} by chat = {}", userId, chatId);
        if (this.hasUser(userId) && !this.hasUserAddress(userId)) {
            userData.saveUsersCurrentBotState(userId, Commands.SET_DELIVERY_INFO);
            return sendMessage(textSender.getText(userId, "send_country"), chatId);
        } else {
            return sendMessage(textSender.getText(userId, "error_set_address"), chatId);
        }
    }

    public SendMessage updateUserInfo(Long userId, Long chatId) {
        log.info("Validate for update user information for user = {} by chat = {}", userId, chatId);
        if (this.hasUser(userId)) {
            userData.saveUsersCurrentBotState(userId, Commands.UPDATE_USER_INFO);
            return sendMessage(textSender.getText(userId, "send_name"), chatId);
        } else {
            return sendMessage(textSender.getText(userId, "error_user_update_info"), chatId);
        }
    }

    public SendMessage updateUserAddress(Long userId, Long chatId) {
        log.info("Validate for set user address information for user = {} by chat = {}", userId, chatId);
        if (this.hasUserAddress(userId) && this.hasUser(userId)) {
            userData.saveUsersCurrentBotState(userId, Commands.UPDATE_DELIVERY_INFO);
            return sendMessage(textSender.getText(userId, "send_country"), chatId);
        } else {
            if (!this.hasUser(userId)) {
                return sendMessage(textSender.getText(userId, "error_update_delivery_info_by_user"), chatId);
            }
            if (!this.hasUserAddress(userId)) {
                return sendMessage(textSender.getText(userId, "error_update_delivery_info_by_address"), chatId);
            }
        }
        return sendMessage(textSender.getText(userId, "error_update"), chatId);
    }
}
