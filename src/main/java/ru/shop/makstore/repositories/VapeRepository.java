package ru.shop.makstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shop.makstore.model.Vape;

public interface VapeRepository extends JpaRepository<Vape, Integer> {
}