package com.example.tg_bot.service.handlers.userhandler.processing;

import com.example.tg_bot.entities.User;
import com.example.tg_bot.entities.dto.UserDto;
import com.example.tg_bot.repo.UserRepository;
import com.example.tg_bot.service.handlers.InfoHandler;
import com.example.tg_bot.utils.cache.UserData;
import com.example.tg_bot.utils.commands.Commands;
import com.example.tg_bot.utils.text.TextSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.example.tg_bot.utils.sendmessage.Sending.sendMessage;

@Service
@RequiredArgsConstructor
public class UserProcessing implements InfoHandler {
    private final UserData userData;
    private final UserRepository userRepository;
    private final UserDto userDto;
    private final TextSender textSender;

    @Override
    public SendMessage saveInfo(Message message) {
        Long chatId = message.getChatId();
        Long userId = message.getFrom().getId();
        UserDto usersInfoState = userData.getUsersInfoState(userId) == null
                ? new UserDto()
                : userData.getUsersInfoState(userId);

        if (usersInfoState.getName() == null) {
            userDto.setName(message.getText());
            userData.saveUsersInfoState(userId, userDto);
            return sendMessage(textSender.getText(userId, "send_surname"), chatId);
        }
        if (usersInfoState.getSurname() == null) {
            usersInfoState.setSurname(message.getText());
            userData.saveUsersInfoState(userId, userDto);
            return sendMessage(textSender.getText(userId, "send_patronymic"), chatId);
        }
        if (usersInfoState.getPatronymic() == null) {
            usersInfoState.setPatronymic(message.getText());
            userData.saveUsersInfoState(userId, userDto);
            return sendMessage(textSender.getText(userId, "send_age"), chatId);
        }
        if (usersInfoState.getAge() == null) {
            try {
                if (Integer.parseInt(message.getText()) < 12 || Integer.parseInt(message.getText()) > 125) {
                    return sendMessage(textSender.getText(userId, "error_age"), chatId);
                } else {
                    usersInfoState.setAge(Integer.parseInt(message.getText()));
                }
            } catch (NumberFormatException ex) {
                return sendMessage(textSender.getText(userId, "error_age"), chatId);
            }
            userData.saveUsersInfoState(userId, userDto);
            userData.saveUsersCurrentBotState(userId, Commands.INFO);
            buildUserAndSave(message.getFrom().getId());
        }
        return sendMessage(textSender.getText(userId, "thanks_message"), chatId);
    }

    @Override
    public SendMessage updateInfo(Message message) {
        Long chatId = message.getChatId();
        Long userId = message.getFrom().getId();
        UserDto usersInfoState = userData.getUsersInfoState(userId) != null
                ? userData.getUsersInfoState(userId)
                : new UserDto();
        if (usersInfoState.getName() == null) {
            userDto.setName(message.getText());
            userData.saveUsersInfoState(userId, userDto);
            return sendMessage(textSender.getText(userId, "send_surname"), chatId);
        }
        if (usersInfoState.getSurname() == null) {
            usersInfoState.setSurname(message.getText());
            userData.saveUsersInfoState(userId, userDto);
            return sendMessage(textSender.getText(userId, "send_patronymic"), chatId);
        }
        if (usersInfoState.getPatronymic() == null) {
            usersInfoState.setPatronymic(message.getText());
            userData.saveUsersInfoState(userId, userDto);
            return sendMessage(textSender.getText(userId, "send_age"), chatId);
        }
        if (usersInfoState.getAge() == null) {
            try {
                if (Integer.parseInt(message.getText()) < 12 || Integer.parseInt(message.getText()) > 125) {
                    return sendMessage(textSender.getText(userId, "error_age"), chatId);
                } else {
                    usersInfoState.setAge(Integer.parseInt(message.getText()));
                }
            } catch (NumberFormatException ex) {
                return sendMessage(textSender.getText(userId, "error_age"), chatId);
            }
            userData.saveUsersInfoState(userId, userDto);
            userData.saveUsersCurrentBotState(userId, Commands.INFO);
            buildUserAndUpdate(message.getFrom().getId());
        }
        return sendMessage(textSender.getText(userId, "thanks_message"), chatId);
    }

    @Override
    public String checkInfo(Long userId) {
        userData.saveUsersCurrentBotState(userId, Commands.INFO);
        if (userRepository.findByUserId(userId).isPresent()){
            User user = userRepository.findByUserId(userId).get();
            return textSender.getText(userId, "name_info") + user.getName()
                    + "\n" + textSender.getText(userId, "surname_info") + user.getSurname()
                    + "\n" + textSender.getText(userId, "patronymic_info") + user.getPatronymic()
                    + "\n" + textSender.getText(userId, "age_info") + user.getAge();
        }
        return textSender.getText(userId, "error_user_check_info");
    }

    private void buildUserAndSave(Long userId) {
        UserDto usersInfoState = userData.getUsersInfoState(userId);
        User newUser = User.builder()
                .userId(userId)
                .name(usersInfoState.getName())
                .surname(usersInfoState.getSurname())
                .patronymic(usersInfoState.getPatronymic())
                .age(usersInfoState.getAge())
                .build();
        userRepository.save(newUser);
    }

    private void buildUserAndUpdate(Long userId) {
        UserDto usersInfoState = userData.getUsersInfoState(userId);
        var user = userRepository.findByUserId(userId);
        if (user.isPresent()) {
            User toUpdateUser = user.get();
            toUpdateUser.setName(usersInfoState.getName());
            toUpdateUser.setSurname(usersInfoState.getSurname());
            toUpdateUser.setPatronymic(usersInfoState.getPatronymic());
            toUpdateUser.setAge(usersInfoState.getAge());
            userRepository.save(toUpdateUser);
        }
    }
}
