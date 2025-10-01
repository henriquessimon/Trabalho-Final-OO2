package br.edu.ifrs.canoas.gamestore.controllers;

import br.edu.ifrs.canoas.gamestore.model.domain.Genre;
import br.edu.ifrs.canoas.gamestore.service.GenreService;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/genre")
@CrossOrigin(origins = "*")
public class GenreController {
   @Autowired
   private GenreService genreService;


   @GetMapping("/getAll")
   public ResponseEntity<List<Genre>> getAllGenres() {
       return ResponseEntity.ok(genreService.geAllGenres());
   }

   @GetMapping("/getOne/{name}")
   public ResponseEntity<Genre> getOnde(@PathVariable String name) {
       Genre genre = genreService.getOne(name);
       if (genre == null) {
        return ResponseEntity.notFound().build();
       } else {
        return ResponseEntity.ok(genre);
       }
   }
   
   @PostMapping
   public ResponseEntity<Genre> createGenre(@RequestBody Genre genre) {
       Genre savedGenre = genreService.createGenre(genre);
        return ResponseEntity.ok(savedGenre);
   }

   @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        genreService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Genre> updateGenre(@PathVariable Long id, @RequestBody Genre updatedGenre) {
        Genre genre = genreService.updateGenre(id, updatedGenre);
        return ResponseEntity.ok(genre);
    }

}