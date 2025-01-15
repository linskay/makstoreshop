package ru.shop.makstore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.shop.makstore.enumtypes.ProductType;

import java.util.Objects;

@Setter
@Getter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String description;

    private int priceRetail;

    private int priceWhole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType type;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    private String imageUrl;

    private int retailQuantity;

    private int wholeQuantity;

    public Product() {
    }

    public Product(Integer id, String name, String description, int priceRetail, int priceWhole,
                   ProductType type, Image image, String imageUrl, int retailQuantity, int wholeQuantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priceRetail = priceRetail;
        this.priceWhole = priceWhole;
        this.type = type;
        this.image = image;
        this.imageUrl = imageUrl;
        this.retailQuantity = retailQuantity;
        this.wholeQuantity = wholeQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return priceRetail == product.priceRetail
                && priceWhole == product.priceWhole
                && retailQuantity == product.retailQuantity
                && wholeQuantity == product.wholeQuantity
                && Objects.equals(id, product.id)
                && Objects.equals(name, product.name)
                && Objects.equals(description, product.description)
                && type == product.type && Objects.equals(image, product.image)
                && Objects.equals(imageUrl, product.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, priceRetail, priceWhole,
                type, image, imageUrl, retailQuantity, wholeQuantity);
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
                ", image=" + image +
                ", imageUrl='" + imageUrl + '\'' +
                ", retailQuantity=" + retailQuantity +
                ", wholeQuantity=" + wholeQuantity +
                '}';
    }
}