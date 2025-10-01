package br.edu.ifrs.canoas.gamestore.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifrs.canoas.gamestore.dto.DevDTO;
import br.edu.ifrs.canoas.gamestore.model.domain.Dev;
import br.edu.ifrs.canoas.gamestore.model.repository.DevRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class DevService {
        
    @Autowired
    private DevRepository devRepository;

    public List<DevDTO> getAllDev() {
        return devRepository.findAll()
            .stream()
            .map(dev -> new DevDTO(dev.getId(), dev.getName()))
            .collect(Collectors.toList());
    }

    public Dev getOne(String name) {
        return devRepository.findByName(name);
    }

    public Optional<Dev> getDev(Long id) {
        return devRepository.findById(id);
    }

    public Dev createDev(Dev dev) {
        return devRepository.save(dev);
    }

    public void deleteById(Long id) {
        Dev dev = devRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Desenvolvedora não encontrada"));
        devRepository.delete(dev);
    }

    public Dev updateDev(Long id, Dev updatedDev) {
        Dev existingDev = devRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dev com ID " + id + " não encontrada"));

        existingDev.setName(updatedDev.getName());
        return devRepository.save(existingDev);
    }
}
