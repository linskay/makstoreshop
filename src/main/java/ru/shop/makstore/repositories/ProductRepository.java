package ru.shop.makstore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.shop.makstore.enumtypes.ProductType;
import ru.shop.makstore.model.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);

    Optional<Product> findByName(String name);

    // Метод для фильтрации по типу товара
    Page<Product> findByType(ProductType type, Pageable pageable);
}