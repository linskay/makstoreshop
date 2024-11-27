package ru.shop.makstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shop.makstore.model.Image;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long>{
    Optional<Image> findByElectronicCigarettesId(Integer electronicCigarettesId);
    Optional<Image> findByVapeId(Integer vapeId);
    Optional<Image> findByOtherId(Integer otherId);

}

