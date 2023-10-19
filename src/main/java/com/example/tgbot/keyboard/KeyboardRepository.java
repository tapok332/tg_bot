package com.example.tgbot.keyboard;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyboardRepository extends ListCrudRepository<Keyboard, Long> {
}
