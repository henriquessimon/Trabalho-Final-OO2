package br.edu.ifrs.canoas.gamestore.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifrs.canoas.gamestore.model.domain.Platform;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {

    public Optional<Platform> findById(Long id);
    public Platform findByName(String name);
}