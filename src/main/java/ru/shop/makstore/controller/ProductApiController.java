package ru.shop.makstore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shop.makstore.model.Product;
import ru.shop.makstore.service.ProductService;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product API", description = "API для управления товарами")
public class ProductApiController {

    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    @Operation(
            summary = "Получить все товары",
            description = "Возвращает список всех товаров.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный ответ"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            }
    )
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить товар по ID",
            description = "Возвращает товар по его уникальному идентификатору.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Товар найден"),
                    @ApiResponse(responseCode = "404", description = "Товар не найден")
            }
    )
    public ResponseEntity<Product> getProductById(
            @Parameter(description = "ID товара", required = true, example = "1")
            @PathVariable Integer id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}