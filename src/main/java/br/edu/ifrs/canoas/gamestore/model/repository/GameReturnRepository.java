package br.edu.ifrs.canoas.gamestore.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifrs.canoas.gamestore.model.domain.GameReturn;

@Repository
public interface GameReturnRepository extends JpaRepository<GameReturn, Long> {

    List<GameReturn> findAll();

    boolean existsByGame_IdAndPurchase_Id(Long gameId, Long purchaseId);

}