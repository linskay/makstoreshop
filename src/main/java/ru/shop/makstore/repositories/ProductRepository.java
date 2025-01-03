package ru.shop.makstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shop.makstore.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
