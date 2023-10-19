package com.example.tgbot.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByTgUserId(Long tgUserId);

    void deleteByTgUserId(Long tgUserId);
}
