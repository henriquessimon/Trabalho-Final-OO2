package br.edu.ifrs.canoas.gamestore.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.edu.ifrs.canoas.gamestore.dto.DevDTO;
import br.edu.ifrs.canoas.gamestore.dto.GameDTO;
import br.edu.ifrs.canoas.gamestore.dto.GameMapper;
import br.edu.ifrs.canoas.gamestore.dto.GamePurchaseDTO;
import br.edu.ifrs.canoas.gamestore.dto.GenreDTO;
import br.edu.ifrs.canoas.gamestore.dto.PlatformDTO;
import br.edu.ifrs.canoas.gamestore.model.domain.Game;
import br.edu.ifrs.canoas.gamestore.model.domain.Genre;
import br.edu.ifrs.canoas.gamestore.model.domain.Platform;
import br.edu.ifrs.canoas.gamestore.model.domain.Purchase;
import br.edu.ifrs.canoas.gamestore.model.domain.PurchaseItem;
import br.edu.ifrs.canoas.gamestore.model.domain.Specification.GameSpecifications;
import br.edu.ifrs.canoas.gamestore.model.repository.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlatformService platformService;

    @Autowired
    private GenreService genreService;

    private PurchaseService purchaseService;

    @Autowired
    public void setPurchaseService(@Lazy PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    public Game createGame(Game game) {
        return gameRepository.save(game);
    }

    @Transactional
    public void deleteGame(Long id) {
        Game game = gameRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Jogo não encontrado"));

        game.getGenres().clear();
        game.getPlatforms().clear();
        gameRepository.save(game);

        // Remove referências em compras
        purchaseService.handleGameRemovalFromPurchases(game);

        gameRepository.delete(game);
    }




    public GameDTO getGameDTOByName(String name) {
        Game game = gameRepository.findByName(name);
        if (game == null) {
            return null;
        }
        return GameMapper.toGameDTO(game);
    }

    public Game getGameEntityByName(String name) {
        return gameRepository.findByName(name);
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Optional<Game> getGameById(Long id) {
        return gameRepository.findById(id);
    }

    public List<GameDTO> getGameByName(String name) {
    List<Game> games = gameRepository.findByNameContainingIgnoreCase(name);
    return games.stream()
        .map(GameMapper::toGameDTO)
        .collect(Collectors.toList());
    }

    public List<GamePurchaseDTO> getGameToAddCartByName(String name) {
        return gameRepository.findGamePurchaseByName(name);
    }

    public List<GameDTO> filterGames(List<Long> genres, List<Long> platforms, List<Long> devs) {
        Specification<Game> spec = Specification.where(null);

        if (genres != null && !genres.isEmpty()) {
            spec = spec.and(GameSpecifications.hasGenres(genres));
        }
        if (platforms != null && !platforms.isEmpty()) {
            spec = spec.and(GameSpecifications.hasPlatforms(platforms));
        }
        if (devs != null && !devs.isEmpty()) {
            spec = spec.and(GameSpecifications.hasDevs(devs));
        }

        return gameRepository.findAll(spec)
            .stream()
            .map(GameMapper::toGameDTO)
            .collect(Collectors.toList());
    }

    public Game updateGame(Long id, GameDTO gameDTO) {
        Game game = gameRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Game not Found"));

        game.setName(gameDTO.getName());
        game.setCoverImage(gameDTO.getCoverImage());
        game.setPrice(gameDTO.getPrice());
        game.setTrailerUrl(gameDTO.getTrailer());
        game.setDescription(gameDTO.getDescription());

        Set<Genre> genres = gameDTO.getGenresID().stream()
            .map(g -> genreService.findById(g.getId())
                .orElseThrow(() -> new EntityNotFoundException("Gênero não encontrado: " + g.getId())))
            .collect(Collectors.toSet());

        Set<Platform> platforms = gameDTO.getPlatformsID().stream()
            .map(p -> platformService.findById(p.getId())
                .orElseThrow(() -> new EntityNotFoundException("Plataforma não encontrada: " + p.getId())))
            .collect(Collectors.toSet());

        game.setGenres(genres);
        game.setPlatforms(platforms);

        return gameRepository.save(game);
    }
}