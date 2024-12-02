package ru.shop.makstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shop.makstore.enumtypes.ProductType;
import ru.shop.makstore.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product findByNameIgnoreCase(String name);
    List<Product> findByDescriptionContainsIgnoreCase(String description);
    List<Product> findByPriceRetail(Integer priceRetail);
    List<Product> findByPriceWhole(Integer priceWhole);
    List<Product> findByType(ProductType productType);
}
