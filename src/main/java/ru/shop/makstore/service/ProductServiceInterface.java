package ru.shop.makstore.service;

import ru.shop.makstore.model.Product;

import java.util.Collection;
import java.util.List;

public interface ProductServiceInterface {
     //crud (create read update delete)
    Product createProduct(Product product);

    Product editProduct(Product productForUpdate);

    Product deleteProduct(int id);
    Product findProduct(int id);

    Collection<Product> getProduct();
    Product findByName(String name);
    Collection<Product> findByDescription(String description);
    Collection<Product> findByPriceRetail(Integer priceRetail);
    Collection<Product> findByPriceWhole(Integer priceWhole);

}
