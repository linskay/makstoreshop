package ru.shop.makstore.model;

import jakarta.persistence.*;

import java.util.Objects;
@Entity
@Table(name = "electronic_cigarettes")
public class ElectronicCigarettes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private boolean retail;
    private int price;
    @OneToOne
    @JoinColumn(name = "image_id", nullable = true)
    private Image image;
    public ElectronicCigarettes() {
    }

    public ElectronicCigarettes(int id, String name, String description, boolean retail, int price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.retail = retail;
        this.price = price;
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

    public boolean isRetail() {
        return retail;
    }

    public void setRetail(boolean retail) {
        this.retail = retail;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectronicCigarettes that = (ElectronicCigarettes) o;
        return id == that.id && retail == that.retail && price == that.price && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, retail, price);
    }

    @Override
    public String toString() {
        return "ElectronicCigarettes{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", retail=" + retail +
                ", price=" + price +
                '}';
    }
}

