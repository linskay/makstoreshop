package ru.shop.makstore.service;

import ru.shop.makstore.model.Product;
import ru.shop.makstore.model.ShoppingCart;

import java.util.List;

public interface ShoppingCartServiceInterface {
    double getPriceForProduct(int productId, boolean isWholesale);

    void addProductToCart(int telegramId, Product product, int quantityInPieces, boolean price);

    void removeProductFromCart(int telegramId, int idProduct);

    void updateProductQuantity(int telegramId, int idProduct, int quantityInPieces);

    List<ShoppingCart> getCartItems(int telegramId);

    int calculateTotalPrice(int telegramId);


}
