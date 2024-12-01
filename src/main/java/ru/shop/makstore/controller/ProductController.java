package ru.shop.makstore.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.shop.makstore.model.Image;
import ru.shop.makstore.model.Product;
import ru.shop.makstore.repositories.ProductRepository;
import ru.shop.makstore.service.ImageService;
import ru.shop.makstore.service.ProductService;

import java.io.IOException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ImageService imageService;

    public ProductController(ProductService productService,
                             ProductRepository productRepository,
                             ImageService imageService) {
        this.productService = productService;
        this.productRepository = productRepository;
        this.imageService = imageService;
    }

    @GetMapping("{id}")    // получает по id
    public ResponseEntity<Product> getProductInfo(@PathVariable int id) {
        Product product =
                productService.findProduct(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity findProduct(@RequestParam(required = false) String name,
                                      @RequestParam(required = false) String description,
                                      @RequestParam(required = false) Integer priceRetail,
                                      @RequestParam(required = false) Integer priceWhole
    ) {
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(productService.findByName(name));
        }
        if (description != null && !description.isBlank()) {
            return ResponseEntity.ok(productService.findByDescription(description));
        }
        if (priceRetail != null) {
            return ResponseEntity.ok(productService.findByPriceRetail(priceRetail));
        }
        if (priceWhole != null) {
            return ResponseEntity.ok(productService.findByPriceWhole(priceWhole));
        }
        return ResponseEntity.ok(productService.getProduct());
    }

    @PostMapping
    public Product addProduct(Product product) {
        return productService.createProduct(product);
    }

    @PutMapping
    public ResponseEntity<Product> editProduct
            (Product product) {
        Product product1 =
                productService.editProduct(product);
        if (product1 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product1);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addImage(@PathVariable int id, @RequestParam MultipartFile image) {
        try {
            imageService.uploadImage(id, image);
            return ResponseEntity.ok("Image uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image: " + e.getMessage());
        }
    }

    @GetMapping(value = "/{id}/save/image")
    public ResponseEntity<byte[]> downloadImage(@PathVariable int id) {
        try {
            Image image = imageService.findImage(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(image.getMediaType()));
            headers.setContentLength(image.getSavesDataInDb().length);
            return ResponseEntity.ok().headers(headers).body(image.getSavesDataInDb());
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
