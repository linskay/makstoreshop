package ru.shop.makstore.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.shop.makstore.enumtypes.ProductType;
import ru.shop.makstore.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductServiceInterface {
    Product findProductById(int productId);
    Product createProduct(Product product);
    Product updateProduct(int id, Product productForUpdate);
    void deleteProduct(int id);
    Optional<Product> getProductById(int id);
    List<Product> getAllProducts();
    Product findProduct(Integer id);

    Page<Product> getAllProducts(Pageable pageable, ProductType type);

    Optional<Product> findByName(String name);
}