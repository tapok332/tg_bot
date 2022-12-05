package com.example.tg_bot.service.handlers.deliveryhandler.processing;

import com.example.tg_bot.entities.dto.DeliveryDto;
import com.example.tg_bot.entities.Delivery;
import com.example.tg_bot.repo.DeliveryRepository;
import com.example.tg_bot.repo.UserRepository;
import com.example.tg_bot.service.handlers.InfoHandler;
import com.example.tg_bot.utils.text.TextSender;
import com.example.tg_bot.utils.cache.UserData;
import com.example.tg_bot.utils.commands.Commands;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.transaction.Transactional;

import static com.example.tg_bot.utils.sendmessage.Sending.sendMessage;

@Service
@RequiredArgsConstructor
public class DeliveryProcessing implements InfoHandler {
    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;
    private final UserData userData;
    private final DeliveryDto deliveryDto;
    private final TextSender textSender;

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
            return sendMessage(textSender.getText(userId, "send_region"), chatId);
        }
        if (usersDeliveryInfoState.getRegion() == null) {
            deliveryDto.setRegion(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_city"), chatId);
        }
        if (usersDeliveryInfoState.getCity() == null) {
            deliveryDto.setCity(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_street"), chatId);
        }
        if (usersDeliveryInfoState.getStreet() == null) {
            deliveryDto.setStreet(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_post_code"), chatId);
        }
        if (usersDeliveryInfoState.getPostCode() == null) {
            deliveryDto.setPostCode(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_delivery_company"), chatId);
        }
        if (usersDeliveryInfoState.getDeliveryCompany() == null) {
            deliveryDto.setDeliveryCompany(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_post_office"), chatId);
        }
        if (usersDeliveryInfoState.getPostalOffice() == null) {
            try {
                deliveryDto.setPostalOffice(Integer.parseInt(message.getText()));
            } catch (NumberFormatException ex){
                return sendMessage(textSender.getText(userId, "error_post_office"), chatId);
            }
            finally {
                userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            }
            return sendMessage(textSender.getText(userId, "send_mail"), chatId);
        }
        if (usersDeliveryInfoState.getMail() == null) {
            deliveryDto.setMail(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_phone_number"), chatId);
        }
        if (usersDeliveryInfoState.getPhoneNum() == null) {
            deliveryDto.setPhoneNum(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            buildDeliveryAndSave(userId);
        }
        userData.saveUsersCurrentBotState(userId, Commands.INFO);

        return sendMessage(textSender.getText(userId, "thanks_message"), chatId);
    }

    @Override
    public SendMessage updateInfo(Message message) {
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();
        DeliveryDto usersDeliveryInfoState = userData.getUsersDeliveryInfoState(userId);
        if (usersDeliveryInfoState.getCountry() == null) {
            deliveryDto.setCountry(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_region"), chatId);
        }
        if (usersDeliveryInfoState.getRegion() == null) {
            deliveryDto.setRegion(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_city"), chatId);
        }
        if (usersDeliveryInfoState.getCity() == null) {
            deliveryDto.setCity(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_street"), chatId);
        }
        if (usersDeliveryInfoState.getStreet() == null) {
            deliveryDto.setStreet(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_post_code"), chatId);
        }
        if (usersDeliveryInfoState.getPostCode() == null) {
            deliveryDto.setPostCode(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_delivery_company"), chatId);
        }
        if (usersDeliveryInfoState.getDeliveryCompany() == null) {
            deliveryDto.setDeliveryCompany(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_post_office"), chatId);
        }
        if (usersDeliveryInfoState.getPostalOffice() == null) {
            try {
                deliveryDto.setPostalOffice(Integer.parseInt(message.getText()));
            } catch (NumberFormatException ex){
                return sendMessage(textSender.getText(userId, "error_post_office"), chatId);
            }
            finally {
                userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            }
            return sendMessage(textSender.getText(userId, "send_mail"), chatId);
        }
        if (usersDeliveryInfoState.getMail() == null) {
            deliveryDto.setMail(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_phone_number"), chatId);
        }
        if (usersDeliveryInfoState.getPhoneNum() == null) {
            deliveryDto.setPhoneNum(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            buildDeliveryAndUpdate(userId);
        }
        userData.saveUsersCurrentBotState(userId, Commands.INFO);

        return sendMessage(textSender.getText(userId, "thanks_message"), chatId);
    }

    @Override
    public String checkInfo(Long userId) {
        if (userRepository.findByUserId(userId).isPresent() && userRepository.findByUserId(userId).get().getAddress() != null) {
            Long addressId = userRepository.findByUserId(userId).get().getAddress().getId();
            if (deliveryRepository.findById(addressId).isPresent()) {
                Delivery address = deliveryRepository.findById(addressId).get();
               return textSender.getText(userId, "country_info") + address.getCountry()
                       + "\n" + textSender.getText(userId, "region_info") + address.getRegion()
                       + "\n" + textSender.getText(userId, "city_info") + address.getCity()
                       + "\n" + textSender.getText(userId, "street_info") + address.getStreet()
                       + "\n" + textSender.getText(userId, "post_code_info") + address.getPostCode()
                       + "\n" + textSender.getText(userId, "delivery_company_info") + address.getDeliveryCompany()
                       + "\n" + textSender.getText(userId, "post_office_info") + address.getPostalOffice()
                       + "\n" + textSender.getText(userId, "mail_info") + address.getMail()
                       + "\n" + textSender.getText(userId, "phone_number_info") + address.getPhoneNum();
            }
        }
        userData.saveUsersCurrentBotState(userId, Commands.INFO);

        return textSender.getText(userId, "error_address_check_info");
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
            return sendMessage(textSender.getText(userId, "error_user_check_info"), chatId);
        }
        Long addressId = userRepository.findByUserId(userId).get().getAddress().getId();

        deliveryRepository.deleteById(addressId);
        userRepository.deleteByUserId(userId);
        userData.saveUsersCurrentBotState(userId, Commands.INFO);

        return sendMessage(textSender.getText(userId, "deleted_info"), chatId);
    }
}
