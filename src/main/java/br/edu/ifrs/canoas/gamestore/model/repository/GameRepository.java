package br.edu.ifrs.canoas.gamestore.model.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.ifrs.canoas.gamestore.dto.GamePurchaseDTO;
import br.edu.ifrs.canoas.gamestore.model.domain.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {

    List<Game> findAll();

    Optional<Game> findById(Long id);

    Game findByName(String name);

    List<Game> findByNameContainingIgnoreCase(String name);

    List<Game> findByPriceBetween(BigDecimal min, BigDecimal max);

    @Query("SELECT new br.edu.ifrs.canoas.gamestore.dto.GamePurchaseDTO(g.name, g.price) FROM Game g WHERE g.name LIKE %:name%")
    List<GamePurchaseDTO> findGamePurchaseByName(@Param("name") String name);
}