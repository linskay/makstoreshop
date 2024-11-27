package ru.shop.makstore.model;

import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.Objects;

import  lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "other")
public class Other {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    private String name;
    @Setter
    @Getter
    private String description;
    @Setter
    @Getter
    private int price;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Other(int id, String name, String description, int price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Other() {
    }

    @Override
    public String toString() {
        return "Other{" +
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
        Other other = (Other) o;
        return id == other.id && price == other.price
                && Objects.equals(name, other.name)
                && Objects.equals(description, other.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price);
    }
}