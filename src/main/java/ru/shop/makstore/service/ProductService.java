package ru.shop.makstore.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.makstore.model.Image;
import ru.shop.makstore.model.Product;
import ru.shop.makstore.repositories.ImageRepository;
import ru.shop.makstore.repositories.ProductRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final String defaultImageDir = "images/default.jpg";

    public ProductService(ProductRepository productRepository, ImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        // Проверка, есть ли уже изображение
        Optional<Image> image = imageRepository.findByProductId(savedProduct.getId());

        if (image.isEmpty()){
            try {
                Path filePath = Paths.get(defaultImageDir);
                byte[] defaultImage = Files.readAllBytes(filePath);

                Image newImage = new Image();
                newImage.setProduct(savedProduct);
                newImage.setFilePath(defaultImageDir);
                newImage.setFileSize(defaultImage.length);
                newImage.setMediaType("image/jpg");
                newImage.setSavesDataInDb(defaultImage);
                imageRepository.save(newImage);
            } catch (IOException e) {
                throw new RuntimeException("Could not read default image file: " + e.getMessage());
            }
        }
        return savedProduct;
    }

    public Optional<Product> updateProduct(Integer id, Product product) {
        if (productRepository.existsById(id)) {
            product.setId(id);
            return Optional.of(productRepository.save(product));
        }
        return Optional.empty();
    }

    public boolean deleteProduct(Integer id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
