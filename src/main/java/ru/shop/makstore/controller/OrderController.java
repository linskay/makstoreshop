package ru.shop.makstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.shop.makstore.model.Product;
import ru.shop.makstore.service.CartService;
import ru.shop.makstore.service.ExcelService;
import ru.shop.makstore.service.ProductService;
import ru.shop.makstore.service.TelegramBotAdmin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/order")
@Tag(name = "Order API", description = "API для управления корзиной")
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

    @GetMapping("/cart")
    public String viewCart(Model model) {
        var cartItems = cartService.getCartItems();
        int total = cartItems.stream()
                .mapToInt(item -> item.getProduct().getPriceRetail() * item.getRetailQuantity() +
                        item.getProduct().getPriceWhole() * item.getWholeQuantity())
                .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "cart"; // Имя шаблона Thymeleaf (cart.html)
    }

    @PostMapping("/add-to-cart")
    @ResponseBody // сохраняем @ResponseBody, так как этот метод возвращает JSON
    public Map<String, Object> addToCart(
            @RequestParam int productId,
            @RequestParam int retailQuantity,
            @RequestParam int wholeQuantity
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            Product product = productService.findProductById(productId);
            cartService.addProductToCart(product, retailQuantity, wholeQuantity);
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
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
