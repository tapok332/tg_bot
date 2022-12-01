package com.example.tg_bot.repo;

import com.example.tg_bot.entities.Delivery;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends CrudRepository<Delivery, Long> {
}
