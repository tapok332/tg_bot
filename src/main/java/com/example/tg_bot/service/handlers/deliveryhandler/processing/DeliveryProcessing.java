package com.example.tg_bot.service.handlers.deliveryhandler.processing;

import com.example.tg_bot.entities.dto.DeliveryDto;
import com.example.tg_bot.entities.Delivery;
import com.example.tg_bot.repo.DeliveryRepository;
import com.example.tg_bot.repo.UserRepository;
import com.example.tg_bot.service.handlers.InfoHandler;
import com.example.tg_bot.utils.cache.UserData;
import com.example.tg_bot.utils.commands.Commands;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.transaction.Transactional;

import static com.example.tg_bot.utils.text.en.TextsForMessage.*;
import static com.example.tg_bot.utils.utilforsendmessage.Sending.sendMessage;

@Service
@RequiredArgsConstructor
public class DeliveryProcessing implements InfoHandler {
    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;
    private final UserData userData;
    private final DeliveryDto deliveryDto;

    @Override
    public SendMessage saveInfo(Message message) {
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();
        DeliveryDto usersDeliveryInfoState = userData.getUsersDeliveryInfoState(userId) != null
                ? userData.getUsersDeliveryInfoState(userId)
                : new DeliveryDto();
        if (usersDeliveryInfoState.getCountry() == null) {
            deliveryDto.setCountry(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage("Send your region: ", chatId);
        }
        if (usersDeliveryInfoState.getRegion() == null) {
            deliveryDto.setRegion(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage("Send your city: ", chatId);
        }
        if (usersDeliveryInfoState.getCity() == null) {
            deliveryDto.setCity(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage("Send your street: ", chatId);
        }
        if (usersDeliveryInfoState.getStreet() == null) {
            deliveryDto.setStreet(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage("Send your post-code: ", chatId);
        }
        if (usersDeliveryInfoState.getPostCode() == null) {
            deliveryDto.setPostCode(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage("Send your delivery company: ", chatId);
        }
        if (usersDeliveryInfoState.getDeliveryCompany() == null) {
            deliveryDto.setDeliveryCompany(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage("Send your post office number: ", chatId);
        }
        if (usersDeliveryInfoState.getPostalOffice() == null) {
            deliveryDto.setPostalOffice(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage("Send your mail: ", chatId);
        }
        if (usersDeliveryInfoState.getMail() == null) {
            deliveryDto.setMail(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage("Send your phone number: ", chatId);
        }
        if (usersDeliveryInfoState.getPhoneNum() == null) {
            deliveryDto.setPhoneNum(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            buildDeliveryAndSave(userId);
        }
        userData.saveUsersCurrentBotState(userId, Commands.INFO);

        return sendMessage("Thanks for your delivery information.", chatId);
    }

    @Override
    public SendMessage updateInfo(Message message) {
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();
        DeliveryDto usersDeliveryInfoState = userData.getUsersDeliveryInfoState(userId);
        if (usersDeliveryInfoState.getCountry() == null) {
            deliveryDto.setCountry(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage("Send your new region: ", chatId);
        }
        if (usersDeliveryInfoState.getRegion() == null) {
            deliveryDto.setRegion(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage("Send your new city: ", chatId);
        }
        if (usersDeliveryInfoState.getCity() == null) {
            deliveryDto.setCity(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage("Send your new street: ", chatId);
        }
        if (usersDeliveryInfoState.getStreet() == null) {
            deliveryDto.setStreet(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage("Send your new post-code: ", chatId);
        }
        if (usersDeliveryInfoState.getPostCode() == null) {
            deliveryDto.setPostCode(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage("Send your new delivery company: ", chatId);
        }
        if (usersDeliveryInfoState.getDeliveryCompany() == null) {
            deliveryDto.setDeliveryCompany(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage("Send your new post office number: ", chatId);
        }
        if (usersDeliveryInfoState.getPostalOffice() == null) {
            deliveryDto.setPostalOffice(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage("Send your new mail: ", chatId);
        }
        if (usersDeliveryInfoState.getMail() == null) {
            deliveryDto.setMail(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage("Send your new phone number: ", chatId);
        }
        if (usersDeliveryInfoState.getPhoneNum() == null) {
            deliveryDto.setPhoneNum(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            buildDeliveryAndUpdate(userId);
        }
        userData.saveUsersCurrentBotState(userId, Commands.INFO);

        return sendMessage("Thanks for your delivery information.", chatId);
    }

    @Override
    public String checkInfo(Long userId) {
        if (userRepository.findByUserId(userId).isPresent() && userRepository.findByUserId(userId).get().getAddress() != null) {
            Long addressId = userRepository.findByUserId(userId).get().getAddress().getId();
            if (deliveryRepository.findById(addressId).isPresent()) {
                return deliveryRepository.findById(addressId).get().toString();
            }
        }
        userData.saveUsersCurrentBotState(userId, Commands.INFO);

        return ERROR_DELIVERY_CHECK_INFO.getText();
    }

    private void buildDeliveryAndSave(Long userId) {
        DeliveryDto usersDeliveryInfoState = userData.getUsersDeliveryInfoState(userId);
        Delivery newAddress = Delivery.builder()
                .country(usersDeliveryInfoState.getCountry())
                .region(usersDeliveryInfoState.getRegion())
                .city(usersDeliveryInfoState.getCity())
                .street(usersDeliveryInfoState.getStreet())
                .postCode(usersDeliveryInfoState.getPostCode())
                .deliveryCompany(usersDeliveryInfoState.getDeliveryCompany())
                .postalOffice(usersDeliveryInfoState.getPostalOffice())
                .mail(usersDeliveryInfoState.getMail())
                .phoneNum(usersDeliveryInfoState.getPhoneNum())
                .build();
        deliveryRepository.save(newAddress);
        userRepository.findByUserId(userId).ifPresent(user -> {
            user.setAddress(newAddress);
            userRepository.save(user);
        });
    }

    private void buildDeliveryAndUpdate(Long userId) {
        DeliveryDto usersDeliveryInfoState = userData.getUsersDeliveryInfoState(userId);
        var user = userRepository.findByUserId(userId);
        if (user.isPresent()) {
            Delivery address = user.get().getAddress();
            address.setCountry(usersDeliveryInfoState.getCountry());
            address.setRegion(usersDeliveryInfoState.getRegion());
            address.setCity(usersDeliveryInfoState.getCity());
            address.setStreet(usersDeliveryInfoState.getStreet());
            address.setPostCode(usersDeliveryInfoState.getPostCode());
            address.setDeliveryCompany(usersDeliveryInfoState.getDeliveryCompany());
            address.setPostalOffice(usersDeliveryInfoState.getPostalOffice());
            address.setMail(usersDeliveryInfoState.getMail());
            address.setPhoneNum(usersDeliveryInfoState.getPhoneNum());
            deliveryRepository.save(address);
            userRepository.findByUserId(userId).ifPresent(userToUpdate -> {
                userToUpdate.setAddress(address);
                userRepository.save(userToUpdate);
            });
        }
    }

    @Transactional
    public SendMessage deleteAllInfo(Long userId, Long chatId) {
        if(userRepository.findByUserId(userId).isEmpty() || userRepository.findByUserId(userId).get().getAddress() == null){
            return sendMessage(WITHOUT_INFO.getText(), chatId);
        }
        Long addressId = userRepository.findByUserId(userId).get().getAddress().getId();

        deliveryRepository.deleteById(addressId);
        userRepository.deleteByUserId(userId);
        userData.saveUsersCurrentBotState(userId, Commands.INFO);

        return sendMessage(DELETED_INFO.getText(), chatId);
    }
}
