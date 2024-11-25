package ru.shop.makstore.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.shop.makstore.model.Other;
import ru.shop.makstore.repositories.OtherRepository;

import java.util.Optional;

@Service
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
        Optional<Other> existingOtherOptional = otherRepository.findById(id);
        if (existingOtherOptional.isPresent()) {
            Other existingOther = existingOtherOptional.get();
            existingOther.setName(other.getName());
            existingOther.setPrice(other.getPrice());
            existingOther.setDescription(other.getDescription());
            return otherRepository.save(existingOther);
        } else {
            return other;
        }
    }

    @Override
    public boolean deleteOther(int id) {
        Optional<Other> otherOptional = otherRepository.findById(id);
        if (otherOptional.isPresent()) {
            otherRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Page<Other> getOthers(Pageable pageable) {
        return otherRepository.findAll(pageable);
    }
}