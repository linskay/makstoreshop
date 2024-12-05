package ru.shop.makstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.shop.makstore.exception.ProductNotFoundException;
import ru.shop.makstore.model.Product;
import ru.shop.makstore.model.ShoppingCart;

import java.util.*;

@Service
public class ShoppingCartService implements ShoppingCartServiceInterface {
    private final ProductService productService;


    @Autowired
    public ShoppingCartService(ProductService productService) {
        this.productService = productService;
    }

    private final Map<Integer, List<ShoppingCart>> carts = new HashMap<>();

    @Override
    public double getPriceForProduct(int productId, boolean isWholesale) {
        Product product = productService.findProductById(productId);
        if (product == null) {
            throw new ProductNotFoundException(productId);
        }
        return isWholesale ? product.getPriceWhole() : product.getPriceRetail();
    }

    @Override
    public void addProductToCart(int telegramId, Product product, int quantityInPieces, boolean isWholesale) {
        // Получаем или создаем корзину для данного telegramId
        List<ShoppingCart> cartItems = carts.computeIfAbsent(telegramId, k -> new ArrayList<>());

        // Проверяем, существует ли уже продукт в корзине
        Optional<ShoppingCart> existingItemOpt = cartItems.stream()
                .filter(item -> item.getIdProduct() == product.getId())
                .findFirst();

        if (existingItemOpt.isPresent()) {
            // Если продукт уже есть в корзине, обновляем его количество
            ShoppingCart existingItem = existingItemOpt.get();
            existingItem.setQuantityInPieces(existingItem.getQuantityInPieces() + quantityInPieces);
        } else {
            // Если продукта нет в корзине, создаем новый элемент
            double price = getPriceForProduct(product.getId(), isWholesale);
            ShoppingCart newItem = new ShoppingCart(product.getId(), quantityInPieces, price, isWholesale);
            cartItems.add(newItem);
        }
    }

    @Override
    public void removeProductFromCart(int telegramId, int idProduct) {
        List<ShoppingCart> cartItems = carts.get(telegramId);
        if (cartItems != null) {
            cartItems.removeIf(item -> item.getIdProduct() == idProduct);
        }
    }

    @Override
    public void updateProductQuantity(int telegramId, int idProduct, int quantityInPieces) {
        List<ShoppingCart> cartItems = carts.get(telegramId);
        if (cartItems != null) {
            for (ShoppingCart item : cartItems) {
                if (item.getIdProduct() == idProduct) {
                    item.setQuantityInPieces(quantityInPieces);
                    break;
                }
            }
        }
    }

    @Override
    public List<ShoppingCart> getCartItems(int telegramId) {
        return carts.getOrDefault(telegramId, new ArrayList<>());
    }

    @Override
    public int calculateTotalPrice(int telegramId) {
        return carts.getOrDefault(telegramId, new ArrayList<>())
                .stream()
                .mapToInt(item -> {
                    if (item.isWholesale()) {
                        return item.getQuantityInPieces() * item.getPriceWhole(); // Оптовая цена
                    } else {
                        return item.getQuantityInPieces() * item.getPriceRetail(); // Розничная цена
                    }
                })
                .sum();
    }
}
