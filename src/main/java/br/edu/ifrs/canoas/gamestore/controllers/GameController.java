package br.edu.ifrs.canoas.gamestore.controllers;

import br.edu.ifrs.canoas.gamestore.dto.GameDTO;
import br.edu.ifrs.canoas.gamestore.dto.GameMapper;
import br.edu.ifrs.canoas.gamestore.dto.GamePurchaseDTO;
import br.edu.ifrs.canoas.gamestore.model.domain.Game;
import br.edu.ifrs.canoas.gamestore.service.GameService;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/game")
@CrossOrigin(origins = "*")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping
    public ResponseEntity<List<GameDTO>> getGameDTO() {
        List<Game> games = gameService.getAllGames();
        List<GameDTO> gameDTOs =  games.stream()
            .map(GameMapper::toGameDTO)
            .collect(Collectors.toList());

            return ResponseEntity.ok(gameDTOs);
    }

    @GetMapping("/search")
    public ResponseEntity<List<GameDTO>> getGamesByName(@RequestParam String name) {
        return ResponseEntity.ok(gameService.getGameByName(name));
    }

    @GetMapping("/getGame/{id}")
    public ResponseEntity<GameDTO> getOne(@PathVariable Long id) {
        return gameService.getGameById(id)
            .map(GameMapper::toGameDTO)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/getOne/{name}")
    public ResponseEntity<GameDTO> getOne(@PathVariable String name) {
        GameDTO gameDTO = gameService.getGameDTOByName(name);
        if (gameDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(gameDTO);
    }


    @GetMapping("/add-cart")
    public ResponseEntity<List<GamePurchaseDTO>> gameAddCar(@RequestParam String name) {
        return ResponseEntity.ok(gameService.getGameToAddCartByName(name));
    }

    @GetMapping("/filter")
    public List<GameDTO> filtersGames(
        @RequestParam(required = false) List<Long> genres,
        @RequestParam(required = false) List<Long> platforms,
        @RequestParam(required = false) List<Long> devs
    ) {
        return gameService.filterGames(genres, platforms, devs);
    }

    @PostMapping
    public ResponseEntity<Game> createGame(@RequestBody Game game) {
        Game savedGame = gameService.createGame(game);
        return ResponseEntity.ok(savedGame);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable Long id, @RequestBody GameDTO gameDTO) {
        try {
            Game updatedGame = gameService.updateGame(id, gameDTO);
            return ResponseEntity.ok(updatedGame);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}