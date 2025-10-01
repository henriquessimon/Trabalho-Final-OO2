package br.edu.ifrs.canoas.gamestore.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.edu.ifrs.canoas.gamestore.model.domain.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    Optional<Purchase> findWithItemsById(Long id);

    Optional<Purchase> findFirstByCompletedFalse();

    Purchase findByUserIdIsNullAndCompletedFalse();

    @Query("SELECT DISTINCT p FROM Purchase p " +
        "JOIN FETCH p.purchaseItems pi " +
        "JOIN FETCH pi.game g " +
        "WHERE p.completed = true")
    List<Purchase> findCompletedPurchasesWithGames();
}