package ru.shop.makstore.enumtypes;

public enum ProductType {
    ELECTRONIC_CIGARETTES("Электронные сигареты"),
    VAPE("Вейп"),
    LIQUID("Жидкость"),
    OTHER("Другое");

    private final String russianName;

    ProductType(String russianName) {
        this.russianName = russianName;
    }

    public String getRussianName() {
        return russianName;
    }

    // Метод для получения типа по русскому названию
    public static ProductType fromRussianName(String russianName) {
        for (ProductType type : ProductType.values()) {
            if (type.getRussianName().equals(russianName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Неизвестное русское название: " + russianName);
    }
}