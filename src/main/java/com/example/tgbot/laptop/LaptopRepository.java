package com.example.tgbot.laptop;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaptopRepository extends ListCrudRepository<Laptop, Long> {
}
