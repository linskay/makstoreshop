package ru.shop.makstore.model;

import jakarta.persistence.*;
import ru.shop.makstore.enumtypes.ProductType;

import java.util.Objects;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private int priceRetail;
    private int priceWhole;
    @Enumerated(EnumType.STRING)
    private ProductType type;

    public Product() {
    }

    public Product(int id, String name, String description,
                   int priceRetail, int priceWhole, ProductType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priceRetail = priceRetail;
        this.priceWhole = priceWhole;
        this.type = type;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public ProductType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && priceRetail == product.priceRetail
                && priceWhole == product.priceWhole && Objects.equals(name, product.name)
                && Objects.equals(description, product.description) && type == product.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, priceRetail, priceWhole, type);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", priceRetail=" + priceRetail +
                ", priceWhole=" + priceWhole +
                ", type=" + type +
                '}';
    }
}
