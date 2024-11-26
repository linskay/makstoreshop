package ru.shop.makstore.interfaces;

import ru.shop.makstore.model.Other;

import java.util.List;

public interface OtherService {

    Other createOther(Other other);

    Other editOther(Other other);

    void deleteOther(int id);

    List<Other> getOthers();
}
