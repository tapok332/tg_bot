package com.example.tg_bot.repo;

import com.example.tg_bot.entities.Keyboard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyboardRepository extends CrudRepository<Keyboard, Long> {
}
