package ru.shop.makstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shop.makstore.model.Product;

import java.util.Collection;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product findByNameIgnoreCase(String name);
    Collection<Product> findByDescriptionContainsIgnoreCase(String description);
    Collection<Product> findByPriceRetail(Integer priceRetail);
    Collection<Product> findByPriceWhole(Integer priceWhole);

}
