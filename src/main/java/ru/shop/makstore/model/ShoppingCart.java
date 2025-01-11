package ru.shop.makstore.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;

@Getter
@Entity
@Table(name = "shopping_cart")
public class ShoppingCart {

    private final List<Product> products = new ArrayList<>();

    public ShoppingCart(int id, int quantityInPieces, double price, boolean isWholesale) {
    }

    public ShoppingCart(int telegramId, int id, String name, int quantityInPieces,
                        int priceRetail, int priceWhole, boolean isWholesale) {
        this.telegramId = telegramId;
        this.idProduct = id;
        this.nameProduct = name;
        this.quantityInPieces = quantityInPieces;
        this.isWholesale = isWholesale;
        this.priceRetail = priceRetail;
        this.priceWhole = priceWhole;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(int telegramId) {
        this.telegramId = telegramId;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public int getQuantityInPieces() {
        return quantityInPieces;
    }

    public void setQuantityInPieces(int quantityInPieces) {
        this.quantityInPieces = quantityInPieces;
    }

    public boolean isWholesale() {
        return isWholesale;
    }

    public void setWholesale(boolean wholesale) {
        isWholesale = wholesale;
    }

    public int getPriceRetail() {
        return priceRetail;
    }

    public void setPriceRetail(int priceRetail) {
        this.priceRetail = priceRetail;
    }

    public int getPriceWhole() {
        return priceWhole;
    }

    public void setPriceWhole(int priceWhole) {
        this.priceWhole = priceWhole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCart that = (ShoppingCart) o;
        return id == that.id && telegramId == that.telegramId && idProduct
                == that.idProduct && quantityInPieces == that.quantityInPieces
                && isWholesale == that.isWholesale && priceRetail == that.priceRetail
                && priceWhole == that.priceWhole && Objects.equals(nameProduct, that.nameProduct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, telegramId, idProduct, nameProduct,
                quantityInPieces, isWholesale, priceRetail, priceWhole);
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "id=" + id +
                ", telegramId=" + telegramId +
                ", idProduct=" + idProduct +
                ", nameProduct='" + nameProduct + '\'' +
                ", quantityInPieces=" + quantityInPieces +
                ", isWholesale=" + isWholesale +
                ", priceRetail=" + priceRetail +
                ", priceWhole=" + priceWhole +
                '}';
    }
}