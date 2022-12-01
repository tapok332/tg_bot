package com.example.tg_bot.validation;

import com.example.tg_bot.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserValidate {

    private final UserRepository userRepository;

    public boolean hasUser(Long userId) {
        return userRepository.findByUserId(userId).isPresent();
    }

    public boolean hasUserAddress(Long userId) {
        return userRepository.findByUserId(userId).isPresent() && userRepository.findByUserId(userId).get().getAddress() != null;
    }
}
