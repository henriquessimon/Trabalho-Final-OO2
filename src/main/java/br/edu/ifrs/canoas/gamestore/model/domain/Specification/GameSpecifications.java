package br.edu.ifrs.canoas.gamestore.model.domain.Specification;

import java.util.List;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import br.edu.ifrs.canoas.gamestore.model.domain.Dev;
import br.edu.ifrs.canoas.gamestore.model.domain.Game;
import br.edu.ifrs.canoas.gamestore.model.domain.Genre;
import br.edu.ifrs.canoas.gamestore.model.domain.Platform;

public class GameSpecifications {
    public static Specification<Game> hasGenres(List<Long> genreIds) {
        return (root, query, builder) -> {
            Join<Game, Genre> genresJoin = root.join("genres");
            return genresJoin.get("id").in(genreIds);
        };
    }

    public static Specification<Game> hasPlatforms(List<Long> platformIds) {
        return (root, query, builder) -> {
            Join<Game, Platform> platformsJoin = root.join("platforms");
            return platformsJoin.get("id").in(platformIds);
        };
    }

    public static Specification<Game> hasDevs(List<Long> devIds) {
        return (root, query, builder) -> {
            Join<Game, Dev> devJoin = root.join("dev");
            return devJoin.get("id").in(devIds);
        };
    }
}
