package ru.shop.makstore.service;

import org.springframework.data.crossstore.ChangeSetPersister;
import ru.shop.makstore.interfaces.OtherService;
import ru.shop.makstore.model.Other;
import ru.shop.makstore.repositories.OtherRepository;

import java.util.List;
import java.util.Optional;

public class OtherServiceImpl implements OtherService {
    private final OtherRepository otherRepository;

    public OtherServiceImpl(OtherRepository otherRepository) {
        this.otherRepository = otherRepository;
    }

    @Override
    public Other createOther(Other other) {
        return otherRepository.save(other);
    }

    @Override
    public Other editOther(Other other) {
        int id = other.getId();
        Optional<Other> existingOther = otherRepository.findById(id);
        existingOther.get().setName(other.getName());
        existingOther.get().setDescription(other.getDescription());
        existingOther.get().setPrice(other.getPrice());
        return otherRepository.save(other);
    }

    @Override
    public void deleteOther(int id) {
        try {
            Other other = otherRepository.findById(id)
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }
        otherRepository.deleteById(id);
    }

    @Override
    public List<Other> getOthers() {
        return List.of();
    }
}
