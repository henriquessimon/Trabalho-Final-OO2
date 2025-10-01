package br.edu.ifrs.canoas.gamestore.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import br.edu.ifrs.canoas.gamestore.dto.GameDTO;
import br.edu.ifrs.canoas.gamestore.dto.GameMapper;
import br.edu.ifrs.canoas.gamestore.dto.PurchaseHistoryDTO;
import br.edu.ifrs.canoas.gamestore.model.domain.Game;
import br.edu.ifrs.canoas.gamestore.model.domain.Purchase;
import br.edu.ifrs.canoas.gamestore.model.domain.PurchaseItem;
import br.edu.ifrs.canoas.gamestore.model.repository.PurchaseItemRepository;
import jakarta.transaction.Transactional;

@Service
public class PurchaseItemService {

    @Autowired
    private PurchaseItemRepository purchaseItemRepository;

    @Autowired
    @Lazy
    private PurchaseService purchaseService;

    public List<PurchaseHistoryDTO> getCompletedPurchaseHistory() {
        List<PurchaseItem> items = purchaseItemRepository.findAllByCompletedPurchases();

        Map<Long, List<PurchaseItem>> grouped = items.stream()
            .collect(Collectors.groupingBy(item -> item.getPurchase().getId()));

        List<PurchaseHistoryDTO> history = new ArrayList<>();

        for (Map.Entry<Long, List<PurchaseItem>> entry : grouped.entrySet()) {
            Purchase purchase = entry.getValue().get(0).getPurchase();

            // Aqui usa seu mapper para criar DTOs completos
            List<GameDTO> gameDTOs = entry.getValue().stream()
                .map(item -> GameMapper.toGameDTO(item.getGame()))
                .toList();

            history.add(new PurchaseHistoryDTO(
                purchase.getId(),
                purchase.getPurchaseDate(),
                purchase.getTotalValue(),
                gameDTOs
            ));
        }

        return history;
    }


    @Transactional
    public void removeItemsByGame(Game game) {
        List<PurchaseItem> items = purchaseItemRepository.findByGame(game);

        for (PurchaseItem item : items) {
            Purchase purchase = item.getPurchase();

            purchase.getPurchaseItems().remove(item);
            purchaseItemRepository.delete(item);

            // Agora delega o recálculo e o salvamento
            purchaseService.recalculateTotal(purchase);
        }
    }

    public void removeItem(PurchaseItem item) {
        purchaseItemRepository.delete(item);
    }
}
