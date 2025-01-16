package ru.shop.makstore.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItem {
    private final Product product;
    private int retailQuantity;
    private int wholeQuantity;

    public CartItem(Product product, int retailQuantity, int wholeQuantity) {
        this.product = product;
        this.retailQuantity = retailQuantity;
        this.wholeQuantity = wholeQuantity;
    }
}