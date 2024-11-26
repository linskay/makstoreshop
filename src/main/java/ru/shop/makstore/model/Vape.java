package ru.shop.makstore.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "vape")
public class Vape {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;
    private int price;
    @OneToOne
    @JoinColumn(name = "image_id", nullable = true)
    private Image image;

    public Vape(int id, String name, int price, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public Vape() {
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Vape{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vape vape = (Vape) o;
        return id == vape.id && price == vape.price
                && Objects.equals(name, vape.name)
                && Objects.equals(description, vape.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price);
    }
}