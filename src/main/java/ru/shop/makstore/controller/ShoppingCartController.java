//package ru.shop.makstore.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import ru.shop.makstore.exception.ProductNotFoundException;
//import ru.shop.makstore.model.Product;
//import ru.shop.makstore.model.ShoppingCart;
//import ru.shop.makstore.service.ShoppingCartService;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/cart")
//public class ShoppingCartController {
//
//    private final ShoppingCartService shoppingCartService;
//
//    @Autowired
//    public ShoppingCartController(ShoppingCartService shoppingCartService) {
//        this.shoppingCartService = shoppingCartService;
//    }
//
//    @PostMapping("/add")
//    public ResponseEntity<String> addProductToCart(@RequestParam int telegramId,
//                                                   @RequestBody Product product,
//                                                   @RequestParam int quantityInPieces,
//                                                   @RequestParam boolean isWholesale) {
//        // Проверяем количество
//        if (quantityInPieces <= 0) {
//            return ResponseEntity.badRequest().body("Quantity must be greater than zero.");
//        }
//        // Проверяем валидность продукта
//        if (product == null || product.getId() <= 0) {
//            return ResponseEntity.badRequest().body("Invalid product.");
//        }
//        // Получаем цену в зависимости от типа покупки
//        double price = shoppingCartService.getPriceForProduct(product.getId(), isWholesale);
//        // Добавляем продукт в корзину с правильной ценой
//        shoppingCartService.addProductToCart(telegramId, product, quantityInPieces, isWholesale);
//
//        return ResponseEntity.status(HttpStatus.OK).body("Product added to cart successfully.");
//    }
//
//    @DeleteMapping("/remove")
//    public ResponseEntity<String> removeProductFromCart(@RequestParam int telegramId,
//                                                        @RequestParam int idProduct) {
//        try {
//            shoppingCartService.removeProductFromCart(telegramId, idProduct);
//            return ResponseEntity.ok("Product removed from cart successfully.");
//        } catch (ProductNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found in cart.");
//        }
//    }
//
//    @PutMapping("/update")
//    public ResponseEntity<String> updateProductQuantity(@RequestParam int telegramId, // пока не юзаем Олег вахуе--- АЙДИ ТГ УКБЕРЕМНАХ МЫ НЕ ВЫВЕЗЕМ СДЕЛАТЬ НОРМ ТАК ЭТО бля йопт капс бесит. впадлу переписывать
//                                                        @RequestParam int idProduct,
//                                                        @RequestParam int quantityInPieces) {
//        shoppingCartService.updateProductQuantity(telegramId, idProduct, quantityInPieces);
//        return ResponseEntity.ok("Product quantity updated successfully.");
//    }
//
//    @GetMapping("/items")
//    public ResponseEntity<List<ShoppingCart>> getCartItems(@RequestParam int telegramId) {
//        List<ShoppingCart> cartItems = shoppingCartService.getCartItems(telegramId);
//        return ResponseEntity.ok(cartItems);
//    }
//
//    @GetMapping("/total-price")
//    public ResponseEntity<Integer> calculateTotalPrice(@RequestParam int telegramId) {
//        int totalPrice = shoppingCartService.calculateTotalPrice(telegramId);
//        return ResponseEntity.ok(totalPrice);
//    }
//}
