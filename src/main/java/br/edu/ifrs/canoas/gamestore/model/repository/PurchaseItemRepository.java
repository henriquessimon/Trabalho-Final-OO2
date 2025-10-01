package br.edu.ifrs.canoas.gamestore.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.edu.ifrs.canoas.gamestore.model.domain.Game;
import br.edu.ifrs.canoas.gamestore.model.domain.PurchaseItem;

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {

    @Query("SELECT pi FROM PurchaseItem pi " +
       "JOIN FETCH pi.purchase p " +
       "JOIN FETCH pi.game g " +
       "WHERE p.completed = true")
    List<PurchaseItem> findAllByCompletedPurchases();

    List<PurchaseItem> findByGame(Game game);

}