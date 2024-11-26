package ru.shop.makstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shop.makstore.model.ElectronicCigarettes;

import java.util.Collection;

public interface ElectronicCigarettesRepository extends JpaRepository<ElectronicCigarettes, Long> {
    ElectronicCigarettes findByNameIgnoreCase(String name);
    Collection<ElectronicCigarettes> findByDescriptionContainsIgnoreCase(String description);
    Collection<ElectronicCigarettes> findByPrice(Integer price);
}
