package ru.shop.makstore.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContactRequest {

    private String name;
    private String phone;
    private String telegram;
    private String message;
}