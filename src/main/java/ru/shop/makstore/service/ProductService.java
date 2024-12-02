package ru.shop.makstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.makstore.enumtypes.ProductType;
import ru.shop.makstore.exception.ProductNotFoundException;
import ru.shop.makstore.model.Product;
import ru.shop.makstore.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService implements ProductServiceInterface {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> updateProduct(int id, Product productForUpdate) {
        if (productRepository.existsById(id)) {
            productForUpdate.setId(id);
            return Optional.of(productRepository.save(productForUpdate));
        }
        return Optional.empty();
    }

    public boolean deleteProduct(int id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Product> getProductById(int id) {
        return productRepository.findById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product findProduct(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product findByName(String name) {
        return productRepository.findByNameIgnoreCase(name);
    }

    public List<Product> findByDescription(String description) {
        return productRepository.findByDescriptionContainsIgnoreCase(description);
    }

    public List<Product> findByPriceRetail(Integer priceRetail) {
        return productRepository.findByPriceRetail(priceRetail);
    }

    public List<Product> findByPriceWhole(Integer priceWhole) {
        return productRepository.findByPriceWhole(priceWhole);
    }

    public List<Product> findByType(String type) {
        return productRepository.findByType(ProductType.valueOf(type));
    }
}