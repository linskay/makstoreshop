package ru.shop.makstore.interfaces;

import ru.shop.makstore.model.Vape;

import java.util.List;

public interface VapeService {
    Vape createVape(Vape vape);

    Vape editVape(Vape vape);

    Vape deleteVape(Vape vape);

    List<Vape> getAllVapes();
}
