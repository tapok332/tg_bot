package com.example.tgbot.user;

import com.example.tgbot.info.BasicInfoHandler;
import com.example.tgbot.utils.cache.UserData;
import com.example.tgbot.utils.commands.Commands;
import com.example.tgbot.utils.text.TextSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.example.tgbot.utils.sendmessage.Sending.sendMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserProcessing implements BasicInfoHandler {

    private static final String INFO_RESPONSE_FORMAT = """
            %s %s
            %s %s
            %s %s
            %s %s""";
    private final UserData userData;
    private final UserRepository userRepository;
    private final TextSender textSender;

    @Override
    public SendMessage saveInfo(Message message) {
        var chatId = message.getChatId();
        var userId = message.getFrom().getId();
        var messageText = message.getText();
        var user = userRepository.findByTgUserId(userId).orElseGet(() -> userRepository.save(new User(userId)));

        return switch (user.getState()) {
            case START_SET -> setUsername(messageText, chatId, user);
            case SET_NAME -> setSurname(messageText, chatId, user);
            case SET_SURNAME -> setPatronymic(messageText, chatId, user);
            case SET_PATRONYMIC -> setAge(messageText, chatId, user);
            default -> sendMessage(textSender.getText(user.getTgUserId(), "error_unknown"), chatId);
        };
    }

    @Override
    public String checkInfo(Long tgUserId) {
        var user = userRepository.findByTgUserId(tgUserId);
        if (user.isPresent()) {
            return String.format(INFO_RESPONSE_FORMAT, textSender.getText(tgUserId, "name_info"), user.get().getName(),
                    textSender.getText(tgUserId, "surname_info"), user.get().getSurname(),
                    textSender.getText(tgUserId, "patronymic_info"), user.get().getPatronymic(),
                    textSender.getText(tgUserId, "age_info"), user.get().getAge());
        }
        userData.saveUsersCurrentBotState(tgUserId, Commands.INFO);

        return textSender.getText(tgUserId, "error_user_check_info");
    }

    private SendMessage setUsername(String name, Long chatId, User user) {
        var userId = user.getTgUserId();

        user.setName(name);
        log.debug("User with id {} execute set name in chat {}", userId, chatId);
        userRepository.save(user);

        return sendMessage(textSender.getText(userId, "send_surname"), chatId);
    }

    private SendMessage setSurname(String surname, Long chatId, User user) {
        var userId = user.getTgUserId();

        user.setSurname(surname);
        log.debug("User with id {} execute set surname in chat {}", userId, chatId);
        userRepository.save(user);

        return sendMessage(textSender.getText(userId, "send_patronymic"), chatId);
    }

    private SendMessage setPatronymic(String patronymic, Long chatId, User user) {
        var userId = user.getTgUserId();

        user.setPatronymic(patronymic);
        log.debug("User with id {} execute set patronymic in chat {}", userId, chatId);
        userRepository.save(user);

        return sendMessage(textSender.getText(userId, "send_age"), chatId);
    }

    private SendMessage setAge(String age, Long chatId, User user) {
        var userId = user.getTgUserId();
        if (isValidAge(age)) {
            user.setAge(Integer.parseInt(age));
            log.debug("User with id {} execute set age in chat {}", userId, chatId);
            userData.saveUsersCurrentBotState(userId, Commands.INFO);
            userRepository.save(user);

            return sendMessage(textSender.getText(userId, "thanks_user_message"), chatId);
        } else {
            log.debug("User with id {} failed execute set age in chat {}", userId, chatId);
            return sendMessage(textSender.getText(userId, "error_age"), chatId);
        }
    }

    private boolean isValidAge(String message) {
        try {
            if (Integer.parseInt(message) < 12 || Integer.parseInt(message) > 125) {
                return false;
            }
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}
