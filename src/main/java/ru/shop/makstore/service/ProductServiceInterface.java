package ru.shop.makstore.service;

import ru.shop.makstore.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductServiceInterface {
    Product createProduct(Product product);

    Optional<Product> updateProduct(int id, Product productForUpdate); // измененный метод

    boolean deleteProduct(int id); // измененный метод

    Optional<Product> getProductById(int id); // новый метод

    List<Product> getAllProducts(); // измененный метод

    Product findProduct(int id);

    Product findByName(String name);

    List<Product> findByDescription(String description); // измененный метод

    List<Product> findByPriceRetail(Integer priceRetail); // измененный метод

    List<Product> findByPriceWhole(Integer priceWhole); // измененный метод
}

