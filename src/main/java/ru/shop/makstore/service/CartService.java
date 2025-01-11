package ru.shop.makstore.service;

import org.springframework.stereotype.Service;
import ru.shop.makstore.model.Product;
import ru.shop.makstore.model.ShoppingCart;

@Service
public class CartService {
    private final ShoppingCart cart = new ShoppingCart();
    public void addProduct(Product product, int quantity) {

    }
}
