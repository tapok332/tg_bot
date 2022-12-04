package com.example.tg_bot.repo;

import com.example.tg_bot.entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUserId(Long userId);
//    @Modifying
//    @Query("delete user_info from Users_info where user_info.user_Id = :userId")
    void deleteByUserId(Long userId);
}
