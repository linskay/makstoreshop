package ru.shop.makstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shop.makstore.enumtypes.ProductType;
import ru.shop.makstore.exception.ProductNotFoundException;
import ru.shop.makstore.model.Product;
import ru.shop.makstore.service.ProductService;

import java.util.Map;

@RestController
@RequestMapping("/admin/products")
@Tag(name = "Admin Product API", description = "API для управления товарами (доступно только администраторам)")

public class AdminController {

    private final ProductService productService;

    public AdminController(ProductService productService) {
        this.productService = productService;

    }

    @PostMapping("/")
    @Operation(
            summary = "Создать новый товар",
            description = "Создает новый товар на основе переданных данных.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Товар успешно создан"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            }
    )
    public ResponseEntity<Product> createProduct(@RequestBody Map<String, Object> payload) {
        String type = (String) payload.get("type");
        Product product = new Product();
        product.setName((String) payload.get("name"));
        product.setDescription((String) payload.get("description"));
        product.setPriceRetail((Integer) payload.get("priceRetail"));
        product.setPriceWhole((Integer) payload.get("priceWhole"));
        product.setType(ProductType.valueOf(type));

        // Сохраняем продукт в базе данных
        Product savedProduct = productService.createProduct(product);

        return ResponseEntity.ok(savedProduct); // Возвращаем сохранённый продукт с ID
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить товар по ID",
            description = "Удаляет товар по его уникальному идентификатору.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Товар успешно удален"),
                    @ApiResponse(responseCode = "404", description = "Товар не найден"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            }
    )
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID товара", required = true, example = "1")
            @PathVariable Integer id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}