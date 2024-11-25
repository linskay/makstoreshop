package ru.shop.makstore.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.shop.makstore.model.Other;

public interface OtherService {

    Other createOther(Other other);

    Other editOther(Other other);

    boolean deleteOther(int id);

    Page<Other> getOthers(Pageable pageable);
}
