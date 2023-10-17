package com.example.tgbot.delivery;

import com.example.tgbot.info.BasicInfoHandler;
import com.example.tgbot.user.UserRepository;
import com.example.tgbot.utils.cache.UserData;
import com.example.tgbot.utils.commands.Commands;
import com.example.tgbot.utils.text.TextSender;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.example.tgbot.utils.sendmessage.Sending.sendMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryProcessing implements BasicInfoHandler {
    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;
    private final UserData userData;
    private final DeliveryDto deliveryDto;
    private final TextSender textSender;

    @Override
    public SendMessage saveInfo(Message message) {
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();
        String username = message.getFrom().getUserName();
        DeliveryDto usersDeliveryInfoState = userData.getUsersDeliveryInfoState(userId) != null
                ? userData.getUsersDeliveryInfoState(userId)
                : new DeliveryDto();
        if (usersDeliveryInfoState.getCountry() == null) {
            log.info("User {} with id {} execute set country in chat {}", username, userId, chatId);
            deliveryDto.setCountry(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_region"), chatId);
        }
        if (usersDeliveryInfoState.getRegion() == null) {
            log.info("User {} with id {} execute set region in chat {}", username, userId, chatId);
            deliveryDto.setRegion(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_city"), chatId);
        }
        if (usersDeliveryInfoState.getCity() == null) {
            log.info("User {} with id {} execute set city in chat {}", username, userId, chatId);
            deliveryDto.setCity(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_street"), chatId);
        }
        if (usersDeliveryInfoState.getStreet() == null) {
            log.info("User {} with id {} execute set street in chat {}", username, userId, chatId);
            deliveryDto.setStreet(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_post_code"), chatId);
        }
        if (usersDeliveryInfoState.getPostCode() == null) {
            log.info("User {} with id {} execute set post code in chat {}", username, userId, chatId);
            deliveryDto.setPostCode(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_delivery_company"), chatId);
        }
        if (usersDeliveryInfoState.getDeliveryCompany() == null) {
            log.info("User {} with id {} execute set delivery company in chat {}", username, userId, chatId);
            deliveryDto.setDeliveryCompany(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_post_office"), chatId);
        }
        if (usersDeliveryInfoState.getPostalOffice() == null) {
            try {
                deliveryDto.setPostalOffice(Integer.parseInt(message.getText()));
            } catch (NumberFormatException ex) {
                log.info("User {} with id {} failed execute set postal office in chat {}", username, userId, chatId);
                return sendMessage(textSender.getText(userId, "error_post_office"), chatId);
            }
            log.info("User {} with id {} execute set postal office in chat {}", username, userId, chatId);
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_mail"), chatId);
        }
        if (usersDeliveryInfoState.getMail() == null) {
            if (message.getText().matches("^[\\w-]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                deliveryDto.setMail(message.getText());
                log.info("User {} with id {} execute set e-mail in chat {}", username, userId, chatId);
            } else {
                log.info("User {} with id {} failed execute set e-mail in chat {}", username, userId, chatId);
                return sendMessage(textSender.getText(userId, "error_mail"), chatId);
            }
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_phone_number"), chatId);
        }
        if (usersDeliveryInfoState.getPhoneNum() == null) {
            if (message.getText().matches("^[+][0-9]{3}[0-9]{9}$")) {
                deliveryDto.setPhoneNum(message.getText());
                log.info("User {} with id {} execute set phone number in chat {}", username, userId, chatId);
            } else {
                log.info("User {} with id {} failed execute set phone number in chat {}", username, userId, chatId);
                return sendMessage(textSender.getText(userId, "error_phone_num"), chatId);
            }
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
        String username = message.getFrom().getUserName();
        DeliveryDto usersDeliveryInfoState = userData.getUsersDeliveryInfoState(userId);
        if (usersDeliveryInfoState.getCountry() == null) {
            log.info("User {} with id {} execute update country in chat {}", username, userId, chatId);
            deliveryDto.setCountry(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_region"), chatId);
        }
        if (usersDeliveryInfoState.getRegion() == null) {
            log.info("User {} with id {} execute update region in chat {}", username, userId, chatId);
            deliveryDto.setRegion(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_city"), chatId);
        }
        if (usersDeliveryInfoState.getCity() == null) {
            log.info("User {} with id {} execute update city in chat {}", username, userId, chatId);
            deliveryDto.setCity(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_street"), chatId);
        }
        if (usersDeliveryInfoState.getStreet() == null) {
            log.info("User {} with id {} execute update street in chat {}", username, userId, chatId);
            deliveryDto.setStreet(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_post_code"), chatId);
        }
        if (usersDeliveryInfoState.getPostCode() == null) {
            log.info("User {} with id {} execute update post code in chat {}", username, userId, chatId);
            deliveryDto.setPostCode(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_delivery_company"), chatId);
        }
        if (usersDeliveryInfoState.getDeliveryCompany() == null) {
            log.info("User {} with id {} execute update delivery company in chat {}", username, userId, chatId);
            deliveryDto.setDeliveryCompany(message.getText());
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_post_office"), chatId);
        }
        if (usersDeliveryInfoState.getPostalOffice() == null) {
            try {
                deliveryDto.setPostalOffice(Integer.parseInt(message.getText()));
            } catch (NumberFormatException ex) {
                log.info("User {} with id {} failed execute update postal office in chat {}", username, userId, chatId);
                return sendMessage(textSender.getText(userId, "error_post_office"), chatId);
            }
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            log.info("User {} with id {} execute update postal office in chat {}", username, userId, chatId);
            return sendMessage(textSender.getText(userId, "send_mail"), chatId);
        }
        if (usersDeliveryInfoState.getMail() == null) {
            if (message.getText().matches("^[\\w-]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                deliveryDto.setMail(message.getText());
                log.info("User {} with id {} execute update e-mail in chat {}", username, userId, chatId);
            } else {
                log.info("User {} with id {} failed execute update e-mail in chat {}", username, userId, chatId);
                return sendMessage(textSender.getText(userId, "error_mail"), chatId);
            }
            userData.saveUsersDeliveryInfoState(userId, deliveryDto);
            return sendMessage(textSender.getText(userId, "send_phone_number"), chatId);
        }
        if (usersDeliveryInfoState.getPhoneNum() == null) {
            if (message.getText().matches("^[+][0-9]{3}[0-9]{9}$")) {
                deliveryDto.setPhoneNum(message.getText());
                log.info("User {} with id {} execute update phone number in chat {}", username, userId, chatId);
            } else {
                log.info("User {} with id {} failed execute update phone number in chat {}", username, userId, chatId);
                return sendMessage(textSender.getText(userId, "error_phone_num"), chatId);
            }
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
        if (userRepository.findByUserId(userId).isEmpty() || userRepository.findByUserId(userId).get().getAddress() == null) {
            return sendMessage(textSender.getText(userId, "error_user_check_info"), chatId);
        }
        Long addressId = userRepository.findByUserId(userId).get().getAddress().getId();

        deliveryRepository.deleteById(addressId);
        userRepository.deleteByUserId(userId);
        userData.saveUsersCurrentBotState(userId, Commands.INFO);
        log.info("User with id {} was deleted", userId);

        return sendMessage(textSender.getText(userId, "deleted_info"), chatId);
    }
}
