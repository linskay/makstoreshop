package ru.shop.makstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shop.makstore.model.Image;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long>  {
    Optional<Image> findByElectronicCigarettesId(Long electronicCigarettesId);
    Optional<Image> findByVapeId(Long vapeId);
    Optional<Image> findByOtherId(Long otherId);

}
