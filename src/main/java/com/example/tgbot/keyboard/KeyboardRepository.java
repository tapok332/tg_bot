package com.example.tgbot.keyboard;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyboardRepository extends CrudRepository<Keyboard, Long> {
}
