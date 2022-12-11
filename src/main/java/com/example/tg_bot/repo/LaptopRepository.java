package com.example.tg_bot.repo;

import com.example.tg_bot.entities.Laptop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaptopRepository extends CrudRepository<Laptop, Long> {
}
