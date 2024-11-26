package ru.shop.makstore.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ru.shop.makstore.interfaces.ElectronicCigarettesInterface;
import ru.shop.makstore.model.ElectronicCigarettes;
import ru.shop.makstore.repositories.ElectronicCigarettesRepository;

import java.util.Collection;
import java.util.List;

@Service
public final class ElectronicCigarettesService implements ElectronicCigarettesInterface {
    private final ElectronicCigarettesRepository electronicCigarettesRepository;

    public ElectronicCigarettesService(
            ElectronicCigarettesRepository electronicCigarettesRepository) {
        this.electronicCigarettesRepository = electronicCigarettesRepository;
    }

    @Override
    public ElectronicCigarettes createElectronicCigarettes(
            ElectronicCigarettes electronicCigarettes) {
        return electronicCigarettesRepository.save(electronicCigarettes);
    }

    @Override
    public ElectronicCigarettes editElectronicCigarettes(
            ElectronicCigarettes electronicCigarettes) {
        long id = electronicCigarettes.getId();
        electronicCigarettesRepository.findById(id)
                .ifPresent(existingVape -> {
                    existingVape.setName(electronicCigarettes.getName());
                    existingVape.setPrice(electronicCigarettes.getPrice());
                    existingVape.setDescription(electronicCigarettes.getDescription());
                });
        return electronicCigarettesRepository.save(electronicCigarettes);
    }

    @Override
    public void deleteElectronicCigarettes(long id) {
        try {
            electronicCigarettesRepository.findById((long) id)
                    .ifPresentOrElse(
                            vape -> electronicCigarettesRepository.deleteById(vape.getId()),
                            () -> {
                                throw new EntityNotFoundException("Vape with id " + id + " not found");
                            }
                    );
        } catch (Exception e) {
            throw new RuntimeException("Error deleting Vape: " + e.getMessage(), e);
        }

    }

    @Override
    public ElectronicCigarettes findElectronicCigarette(long id) {
        return electronicCigarettesRepository.findById(id).get();
    }

    @Override
    public List<ElectronicCigarettes> getElectronicCigarettes() {
        return electronicCigarettesRepository.findAll();
    }

    @Override
    public ElectronicCigarettes findByName(String name) {
        return electronicCigarettesRepository.findByNameIgnoreCase(name);
    }

    @Override
    public Collection<ElectronicCigarettes> findByDescription(String description) {
        return electronicCigarettesRepository.findByDescriptionContainsIgnoreCase(description);
    }

    @Override
    public Collection<ElectronicCigarettes> findByPrice(Integer price) {
        return electronicCigarettesRepository.findByPrice(price);
    }
}
