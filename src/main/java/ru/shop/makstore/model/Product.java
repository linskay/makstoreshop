package ru.shop.makstore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.shop.makstore.enumtypes.ProductType;

import java.util.Objects;

@Getter
@Entity
public class Product {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Setter
    private String name;
    @Setter
    private String description;
    @Setter
    private int priceRetail;
    @Setter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return getId() == product.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
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
