package ru.shop.makstore.controller;

import jakarta.servlet.http.HttpServletResponse;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

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
    public Product addProduct (Product product) {
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
    public ResponseEntity<String> addImage(@PathVariable int id,
                                           @RequestParam MultipartFile cover) throws IOException {
        if (cover.getSize() >= 3840 * 2160) {
            return ResponseEntity.badRequest().body("Cover file size is too large.");
        }
        imageService.additionImage(id, cover);
        return ResponseEntity.ok().build();
    }
    /*
                   HD 1280×720 (16:9)
                   WXGA 1366×768 (16:9)
                   Full HD 1920×1080 (16:9)
                   WQHD 2560×1440 (16:9)
                   UWQHD 3100×1440 (21:9)
                   4K UHD 3840×2160 (16:9)
                   8K UHD 7680×4320 (16:9)
     */
    @GetMapping(value = "/{id}/DB")
    public ResponseEntity<byte[]> downloadImage(@PathVariable int id) {
        Image image = imageService.findImage(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(image.getMediaType()));
        headers.setContentLength(image.getSavesDataInDb().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(image.getSavesDataInDb());
    }
}
