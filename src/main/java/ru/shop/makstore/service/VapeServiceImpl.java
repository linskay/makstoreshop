package ru.shop.makstore.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ru.shop.makstore.model.Vape;
import ru.shop.makstore.repositories.VapeRepository;

import java.util.List;

@Service
public class VapeServiceImpl implements VapeService {
    private final VapeRepository vapeRepository;

    public VapeServiceImpl(VapeRepository vapeRepository) {
        this.vapeRepository = vapeRepository;
    }

    @Override
    public Vape createVape(Vape vape) {
        return vapeRepository.save(vape);
    }

    @Override
    public Vape editVape(Vape vape) {
        int id = vape.getId();
        vapeRepository.findById(id)
                .ifPresent(existingVape -> {
                    existingVape.setName(vape.getName());
                    existingVape.setPrice(vape.getPrice());
                    existingVape.setDescription(vape.getDescription());
                });
        return vapeRepository.save(vape);
    }

    @Override
    public void deleteVape(int id) {
        try {
            vapeRepository.findById(id)
                    .ifPresentOrElse(
                            vape -> vapeRepository.deleteById(vape.getId()),
                            () -> {
                                throw new EntityNotFoundException("Vape with id " + id + " not found");
                            }
                    );
        } catch (Exception e) {
            throw new RuntimeException("Error deleting Vape: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Vape> getAllVapes() {
        return vapeRepository.findAll();
    }
}
