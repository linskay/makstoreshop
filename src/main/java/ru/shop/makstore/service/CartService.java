package ru.shop.makstore.service;

import org.springframework.stereotype.Service;
import ru.shop.makstore.model.CartItem;
import ru.shop.makstore.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final List<CartItem> cart = new ArrayList<>();

    /**
     * Добавляет товар в корзину.
     *
     * @param product         Товар для добавления.
     * @param retailQuantity  Количество для розницы.
     * @param wholeQuantity   Количество для опта.
     */
    public void addProductToCart(Product product, int retailQuantity, int wholeQuantity) {
        // Проверяем, есть ли уже такой товар в корзине
        Optional<CartItem> existingItem = cart.stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            // Обновляем количество, если товар уже есть в корзине
            CartItem item = existingItem.get();
            item.setRetailQuantity(item.getRetailQuantity() + retailQuantity);
            item.setWholeQuantity(item.getWholeQuantity() + wholeQuantity);
        } else {
            // Добавляем новый товар в корзину
            cart.add(new CartItem(product, retailQuantity, wholeQuantity));
        }
    }

    /**
     * Возвращает список товаров в корзине.
     *
     * @return Список товаров в корзине.
     */
    public List<CartItem> getCartItems() {
        return cart;
    }

    /**
     * Очищает корзину.
     */
    public void clearCart() {
        cart.clear();
    }
}