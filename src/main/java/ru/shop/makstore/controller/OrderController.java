package ru.shop.makstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shop.makstore.model.Product;
import ru.shop.makstore.service.CartService;
import ru.shop.makstore.service.ExcelService;
import ru.shop.makstore.service.ProductService;
import ru.shop.makstore.service.TelegramBotAdmin;

import java.io.File;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final CartService cartService;
    private final ProductService productService;
    private final ExcelService excelService;
    private final TelegramBotAdmin telegramBotAdmin;

    @Value("${telegram.chat.id}")
    private String chatId;

    public OrderController(CartService cartService, ProductService productService, ExcelService excelService, TelegramBotAdmin telegramBotAdmin) {
        this.cartService = cartService;
        this.productService = productService;
        this.excelService = excelService;
        this.telegramBotAdmin = telegramBotAdmin;
    }

    @PostMapping("/add-to-cart")
    @Operation(
            summary = "Добавить товар в корзину",
            description = "Добавляет товар в корзину с указанием количества (розница и опт).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Товар добавлен в корзину"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            })
    public ResponseEntity<String> addToCart(
            @RequestParam int productId, // ID товара
            @RequestParam int retailQuantity, // Количество для розницы
            @RequestParam int wholeQuantity // Количество для опта
    ) {
        try {
            // Получаем товар из базы данных
            Product product = productService.findProductById(productId);

            // Добавляем товар в корзину
            cartService.addProductToCart(product, retailQuantity, wholeQuantity);

            return ResponseEntity.ok("Товар добавлен в корзину.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка при добавлении товара в корзину.");
        }
    }

    @PostMapping("/send-to-telegram")
    @Operation(
            summary = "Отправить заказ в Telegram",
            description = "Формирует Excel-файл и отправляет его в указанный Telegram-чат.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заказ отправлен в Telegram"),
                    @ApiResponse(responseCode = "400", description = "Корзина пуста"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            })
    public ResponseEntity<String> sendToTelegram() {
        var cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return ResponseEntity.badRequest().body("Корзина пуста.");
        }

        // Создаем Excel-файл
        File excelFile = excelService.createExcelFile(cartItems);

        // Отправляем файл в Telegram
        telegramBotAdmin.sendExcelFile(chatId, excelFile);

        // Очищаем корзину
        cartService.clearCart();

        return ResponseEntity.ok("Заказ отправлен в Telegram.");
    }
}