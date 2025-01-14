package ru.shop.makstore.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Objects;

@Setter
@Getter
public class CheckoutRequest {
    private ArrayList<Product> products;
    private ArrayList<Integer> retailQuantities;
    private ArrayList<Integer> wholeQuantities;

    public CheckoutRequest(ArrayList<Product> products,
                           ArrayList<Integer> retailQuantities,
                           ArrayList<Integer> wholeQuantities) {
        this.products = products;
        this.retailQuantities = retailQuantities;
        this.wholeQuantities = wholeQuantities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckoutRequest that = (CheckoutRequest) o;
        return Objects.equals(products, that.products)
                && Objects.equals(retailQuantities, that.retailQuantities)
                && Objects.equals(wholeQuantities, that.wholeQuantities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(products, retailQuantities, wholeQuantities);
    }

    @Override
    public String toString() {
        return "CheckoutRequest{" +
                "products=" + products +
                ", retailQuantities=" + retailQuantities +
                ", wholeQuantities=" + wholeQuantities +
                '}';
    }
}
