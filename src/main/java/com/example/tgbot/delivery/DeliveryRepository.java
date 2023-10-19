package com.example.tgbot.delivery;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryRepository extends CrudRepository<Delivery, Long> {
    Optional<Delivery> findByUserId(Long userId);
}
