package br.edu.ifrs.canoas.gamestore.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifrs.canoas.gamestore.model.domain.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    public Optional<Genre> findById(Long id);
    public Genre findByName(String name);
}