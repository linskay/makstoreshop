package ru.shop.makstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product findProductById(int productId) {
        return productRepository.findById((long) productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(int id, Product productForUpdate) {
        if (productRepository.existsById((long) id)) {
            productForUpdate.setId(id);
            return productRepository.save(productForUpdate);
        }
        throw new ProductNotFoundException(id);
    }

    @Override
    public boolean deleteProduct(int id) {
        if (productRepository.existsById((long) id)) {
            productRepository.deleteById((long) id);
            return true;
        }
        throw new ProductNotFoundException(id);
    }

    @Override
    public Optional<Product> getProductById(int id) {
        return productRepository.findById((long) id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll(); // Возвращаем все продукты без пагинации
    }

    @Override
    public Product findProduct(Integer id) {
        return productRepository.findById((long) id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable, ProductType type) {
        if (type != null) {
            return productRepository.findByType(type, pageable); // Фильтрация по типу
        } else {
            return productRepository.findAll(pageable); // Без фильтрации
        }
    }

    @Override
    public Optional<Product> findByName(String name) {
        return productRepository.findByName(name);
    }
}