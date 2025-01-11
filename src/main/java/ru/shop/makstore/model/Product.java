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

    public Product() {}

    public Product(String name, String description, int priceRetail, int priceWhole, ProductType type) {
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

    public void setType(ProductType type) {
        this.type = type;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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