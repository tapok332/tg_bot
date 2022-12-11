package com.example.tg_bot.repo;

import com.example.tg_bot.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
