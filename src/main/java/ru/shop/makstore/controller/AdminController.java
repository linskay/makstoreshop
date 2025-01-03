package ru.shop.makstore.controller;

import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.shop.makstore.enumtypes.ProductType;
import ru.shop.makstore.model.Product;
import ru.shop.makstore.service.ProductService;

import java.util.Map;

@RestController
@RequestMapping("/admin/products")
//@PreAuthorize("hasRole('ADMIN')") // Только админы имеют доступ ко всем методам
public class AdminController {

    private final ProductService productService;

    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/")
    public ResponseEntity<Product> createProduct(@RequestBody Map<String, Object> payload) {
        String type = (String) payload.get("type");
        Product product = new Product();
        product.setName((String) payload.get("name"));
        product.setDescription((String) payload.get("description"));
        product.setPriceRetail((Integer) payload.get("priceRetail"));
        product.setPriceWhole((Integer) payload.get("priceWhole"));

        product.setType(ProductType.valueOf(type));

        return ResponseEntity.ok(productService.createProduct(product));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        return productService.updateProduct(id, product)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}