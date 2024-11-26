package ru.shop.makstore.interfaces;

import ru.shop.makstore.model.ElectronicCigarettes;

import java.util.Collection;
import java.util.List;

public interface ElectronicCigarettesInterface {
    //crud (create read update delete)
    ElectronicCigarettes createElectronicCigarettes(ElectronicCigarettes electronicCigarettes);

    ElectronicCigarettes editElectronicCigarettes(ElectronicCigarettes electronicCigarettes);

    void deleteElectronicCigarettes(long id);
    ElectronicCigarettes findElectronicCigarette(long id);

    List<ElectronicCigarettes> getElectronicCigarettes();
    ElectronicCigarettes findByName(String name);
    Collection<ElectronicCigarettes> findByDescription(String description);
    Collection<ElectronicCigarettes> findByPrice(Integer price);
}
