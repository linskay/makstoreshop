package ru.shop.makstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.shop.makstore.enumtypes.ProductType;
import ru.shop.makstore.model.Product;
import ru.shop.makstore.service.ProductService;

import java.util.Map;

@RestController
@RequestMapping("/admin/products")
@Tag(name = "Admin Product API", description = "API для управления товарами (доступно только администраторам)")
//@PreAuthorize("hasRole('ADMIN')") // Только админы имеют доступ ко всем методам
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
    public ResponseEntity<Product> createProduct(
            @Parameter(description = "Данные товара в формате JSON", required = true, example = "{\"name\": \"Новый товар\", \"description\": \"Описание товара\", \"priceRetail\": 1000, \"priceWhole\": 800, \"type\": \"ELECTRONIC_CIGARETTES\"}")
            @RequestBody Map<String, Object> payload) {
        String type = (String) payload.get("type");
        Product product = new Product();
        product.setName((String) payload.get("name"));
        product.setDescription((String) payload.get("description"));
        product.setPriceRetail((Integer) payload.get("priceRetail"));
        product.setPriceWhole((Integer) payload.get("priceWhole"));
        product.setType(ProductType.valueOf(type));

        return ResponseEntity.ok(productService.createProduct(product));
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
        if (productService.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}