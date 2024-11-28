package ru.shop.makstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.makstore.exception.ProductNotFoundException;
import ru.shop.makstore.model.Product;
import ru.shop.makstore.repositories.ProductRepository;

import java.util.Collection;

@Service
@Transactional
public class ProductService implements ProductServiceInterface {

    @Autowired
    private ProductRepository productRepository;


    @Override
    public Product createProduct(Product product) { // создает
        return productRepository.save(product);
    }

    @Override
    public Product editProduct(Product productForUpdate) { // редачит
        if (!productRepository.existsById(productForUpdate.getId())) {
            throw new ProductNotFoundException(productForUpdate.getId());
        }
        return productRepository.save(productForUpdate);
    }

    @Override
    public Product deleteProduct(int id) { // удаляет
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.deleteById(product.getId());
        return product;
    }

    @Override
    public Product findProduct(int id) {  //  возвращает продукт по id
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Collection<Product> getProduct() { //  список продуктов
        return productRepository.findAll();
    }

    @Override
    public Product findByName(String name) {  //  по имени
        return productRepository.findByNameIgnoreCase(name);
    }

    @Override
    public Collection<Product> findByDescription(String description) { //  по описанию
        return productRepository.findByDescriptionContainsIgnoreCase(description);
    }

    @Override
    public Collection<Product> findByPriceRetail(Integer priceRetail) { //  по цене розница
        return productRepository.findByPriceRetail(priceRetail);
    }
    @Override
    public Collection<Product> findByPriceWhole(Integer priceWhole) { //  по цене опт
        return productRepository.findByPriceWhole(priceWhole);
    }
}
