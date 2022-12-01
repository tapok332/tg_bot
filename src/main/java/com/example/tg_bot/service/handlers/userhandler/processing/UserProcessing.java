package com.example.tg_bot.service.handlers.userhandler.processing;

import com.example.tg_bot.entities.dto.UserDto;
import com.example.tg_bot.entities.User;
import com.example.tg_bot.repo.UserRepository;
import com.example.tg_bot.service.handlers.InfoHandler;
import com.example.tg_bot.utils.cache.UserData;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.example.tg_bot.utils.text.en.TextsForMessage.ERROR_USER_CHECK_INFO;
import static com.example.tg_bot.utils.utilforsendmessage.Sending.sendMessage;

@Service
@RequiredArgsConstructor
public class UserProcessing implements InfoHandler {
    private final UserData userData;
    private final UserRepository userRepository;
    private final UserDto userDto;

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
            return sendMessage("Send your surname: ", chatId);
        }
        if (usersInfoState.getSurname() == null) {
            usersInfoState.setSurname(message.getText());
            userData.saveUsersInfoState(userId, userDto);
            return sendMessage("Send your patronymic: ", chatId);
        }
        if (usersInfoState.getPatronymic() == null) {
            usersInfoState.setPatronymic(message.getText());
            userData.saveUsersInfoState(userId, userDto);
            return sendMessage("Send your age: ", chatId);
        }
        if (usersInfoState.getAge() == null) {
            try{
                usersInfoState.setAge(Integer.parseInt(message.getText()));
            }catch (NumberFormatException ex){
                return sendMessage("Please send numbers.", chatId);
            }
            userData.saveUsersInfoState(userId, userDto);
            buildUserAndSave(message.getFrom().getId());
        }
        return sendMessage("Thanks for information about you.", chatId);
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
            return sendMessage("Send your new surname: ", chatId);
        }
        if (usersInfoState.getSurname() == null) {
            usersInfoState.setSurname(message.getText());
            userData.saveUsersInfoState(userId, userDto);
            return sendMessage("Send your new patronymic: ", chatId);
        }
        if (usersInfoState.getPatronymic() == null) {
            usersInfoState.setPatronymic(message.getText());
            userData.saveUsersInfoState(userId, userDto);
            return sendMessage("Send your new age: ", chatId);
        }
        if (usersInfoState.getAge() == null) {
            try{
                usersInfoState.setAge(Integer.parseInt(message.getText()));
            }catch (NumberFormatException ex){
                return sendMessage("Please send numbers.", chatId);
            }
            userData.saveUsersInfoState(userId, userDto);
            buildUserAndUpdate(message.getFrom().getId());
        }
        return sendMessage("Thanks for information about you.", chatId);
    }

    @Override
    public String checkInfo(Long userId) {
        return userRepository.findByUserId(userId).isPresent()
                ? userRepository.findByUserId(userId).get().toString()
                : ERROR_USER_CHECK_INFO.getText();
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
