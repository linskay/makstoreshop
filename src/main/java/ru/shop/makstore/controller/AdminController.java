package ru.shop.makstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.shop.makstore.enumtypes.ProductType;
import ru.shop.makstore.model.Product;
import ru.shop.makstore.service.CartService;
import ru.shop.makstore.service.ExcelService;
import ru.shop.makstore.service.ProductService;
import ru.shop.makstore.service.TelegramBotAdmin;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/admin/products")
@Tag(name = "Admin Product API", description = "API для управления товарами (доступно только администраторам)")
//@PreAuthorize("hasRole('ADMIN')") // Только админы имеют доступ ко всем методам
public class AdminController {

    private final ProductService productService;
    private final ExcelService excelService;
    private final TelegramBotAdmin telegramBotAdmin;
    private final CartService cartService;
    @Value("${telegram.chat.id}")
    private String chatId;

    public AdminController(ProductService productService, ExcelService excelService,
                           TelegramBotAdmin telegramBotAdmin, CartService cartService) {
        this.productService = productService;
        this.excelService = excelService;
        this.telegramBotAdmin = telegramBotAdmin;
        this.cartService = cartService;
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

    @PostMapping("/checkout")
    @Operation(
            summary = "Оформить заказ",
            description = "Обрабатывает заказ и отправляет файл Excel в Telegram.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заказ обработан и файл отправлен"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            })
    public ResponseEntity<String> checkout(@RequestParam String telegramId) {
        // Получаем товары из корзины пользователя
        ArrayList<Product> products = cartService.getProductsForUser(telegramId);

        if (products.isEmpty()) {
            return ResponseEntity.badRequest().body("Корзина пуста.");
        }
        // Создаем Excel файл из товаров
        File excelFile = excelService.createExcelFile(products);

        // Отправляем файл в Telegram
        telegramBotAdmin.sendExcelFile(chatId, excelFile);

        // Очистка корзины после оформления заказа
        cartService.clearCartForUser(telegramId);

        return ResponseEntity.ok("Заказ обработан и файл отправлен в Telegram.");
    }
}