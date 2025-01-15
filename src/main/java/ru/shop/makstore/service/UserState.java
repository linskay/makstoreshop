package ru.shop.makstore.service;

public enum UserState {
    AWAITING_NAME,        // Ожидание ввода названия
    AWAITING_DESCRIPTION, // Ожидание ввода описания
    AWAITING_PRICE_RETAIL, // Ожидание ввода розничной цены
    AWAITING_PRICE_WHOLE, // Ожидание ввода оптовой цены
    AWAITING_TYPE,        // Ожидание ввода типа продукта
    AWAITING_IMAGE,       // Ожидание загрузки изображения
    AWAITING_DELETE_ID    // Ожидание ввода ID для удаления продукта
}