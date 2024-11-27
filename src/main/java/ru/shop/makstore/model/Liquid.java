package ru.shop.makstore.model;

import java.util.Objects;

public class Liquid {

    int Id;
    String Name;
    String Description;
    int price;
    int priceOpt;

    public Liquid(int id, String name, String description, int price, int priceOpt) {
        Id = id;
        Name = name;
        Description = description;
        this.price = price;
        this.priceOpt = priceOpt;
    }

    public Liquid() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPriceOpt() {
        return priceOpt;
    }

    public void setPriceOpt(int priceOpt) {
        this.priceOpt = priceOpt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Liquid liquid = (Liquid) o;
        return Id == liquid.Id && price == liquid.price && priceOpt == liquid.priceOpt && Objects.equals(Name, liquid.Name) && Objects.equals(Description, liquid.Description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, Name, Description, price, priceOpt);
    }

    @Override
    public String toString() {
        return "Liquid{" +
                "Id=" + Id +
                ", Name='" + Name + '\'' +
                ", Description='" + Description + '\'' +
                ", price=" + price +
                ", priceOpt=" + priceOpt +
                '}';
    }
}