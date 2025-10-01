package br.edu.ifrs.canoas.gamestore.dto;

import java.util.Set;
import java.util.stream.Collectors;

import br.edu.ifrs.canoas.gamestore.model.domain.Dev;
import br.edu.ifrs.canoas.gamestore.model.domain.Game;

public class GameMapper {
    public static GameDTO toGameDTO(Game game) {

        Dev dev = game.getDev();
        DevDTO devDTO = null;
        if (dev != null) {
            devDTO = new DevDTO(dev.getId(), dev.getName());
        }

        Set<PlatformDTO> platformDTOs = game.getPlatforms()
            .stream()
            .map(p -> new PlatformDTO(p.getId(), p.getName()))
            .collect(Collectors.toSet());

        Set<GenreDTO> genreDTOs = game.getGenres()
            .stream()
            .map(g -> new GenreDTO(g.getId(), g.getName()))
            .collect(Collectors.toSet());
        return new GameDTO(
            game.getId(),
            game.getName(),
            game.getPrice(),
            
            game.getReleaseYear(),
            game.getTrailerUrl(),
            game.getDescription(),
            game.getCoverImage(),
            devDTO,
            platformDTOs,
            genreDTOs
        );
    }
}