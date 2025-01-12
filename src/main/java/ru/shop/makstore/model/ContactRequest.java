package ru.shop.makstore.model;

import lombok.Data;

@Data
public class ContactRequest {

    private String name;
    private String phone;
    private String telegram;
    private String message;
}