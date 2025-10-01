package br.edu.ifrs.canoas.gamestore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifrs.canoas.gamestore.model.domain.Platform;
import br.edu.ifrs.canoas.gamestore.model.repository.PlatformRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class PlatformService {
    
    @Autowired
    private PlatformRepository platformRepository;

    public List<Platform> getAllPlatforms() {
        return platformRepository.findAll();
    }

    public Optional<Platform> findById(Long id) {
        return platformRepository.findById(id);
    }

    public Platform getOne(String name) {
        return platformRepository.findByName(name);
    }

    public Platform createPlatform(Platform platform) {
        return platformRepository.save(platform);
    }

    public Optional<Platform> getPlatform(Long id) {
        return platformRepository.findById(id);
    }

    public void deleteById(Long id) {
        Platform platform = platformRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plataforma não encontrada"));

        // Remove a associação com jogos
        platform.getGames().forEach(game -> game.getPlatforms().remove(platform));

        platformRepository.delete(platform);
    }

     public Platform updatePlatform(Long id, Platform updatedPlatform) {
        Platform existingPlatform = platformRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Plataforma com ID " + id + " não encontrada"));

        existingPlatform.setName(updatedPlatform.getName());
        return platformRepository.save(existingPlatform);
    }
}