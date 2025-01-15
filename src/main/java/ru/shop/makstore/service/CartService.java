package ru.shop.makstore.service;

import org.springframework.stereotype.Service;
import ru.shop.makstore.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import org.springframework.stereotype.Service;
//import ru.shop.makstore.model.Product;
//import ru.shop.makstore.model.ShoppingCart;

@Service
public class CartService {
    private final Map<String, ArrayList<Product>> userCarts = new HashMap<>();

    public ArrayList<Product> getProductsForUser(String userId) {
        return userCarts.getOrDefault(userId, new ArrayList<>());
    }
    public void addProductToCart(String userId, Product product) {
        userCarts.computeIfAbsent(userId, k -> new ArrayList<>()).add(product);
    }
    public void clearCartForUser(String userId) {
        userCarts.remove(userId);
    }
    //    private final ShoppingCart cart = new ShoppingCart();
//    public void addProduct(Product product, int quantity) {
//
//    }
}


