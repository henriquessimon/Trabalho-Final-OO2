package br.edu.ifrs.canoas.gamestore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifrs.canoas.gamestore.model.domain.Genre;
import br.edu.ifrs.canoas.gamestore.model.repository.GenreRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class GenreService {
    @Autowired
    private GenreRepository genreRepository;


    public List<Genre> geAllGenres() {
        return genreRepository.findAll();
    }

    public Optional<Genre> findById(Long id) {
        return genreRepository.findById(id);
    }

    public Genre getOne(String name) {
        return genreRepository.findByName(name);
    }

    public Genre createGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    public Optional<Genre> getGenre(Long id) {
        return genreRepository.findById(id);
    }

    public void deleteById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gênero não encontrado"));

        genre.getGames().forEach(game -> game.getGenres().remove(genre));

        genreRepository.delete(genre);
    }

    public Genre updateGenre(Long id, Genre updatedGenre) {
        Genre existingGenre = genreRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Gênero com ID " + id + " não encontrado"));

        existingGenre.setName(updatedGenre.getName());
        return genreRepository.save(existingGenre);
    }
}