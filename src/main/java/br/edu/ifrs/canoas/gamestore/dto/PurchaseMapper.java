package br.edu.ifrs.canoas.gamestore.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.edu.ifrs.canoas.gamestore.model.domain.Purchase;

public class PurchaseMapper {
    public static PurchaseDTO toDTO(Purchase purchase) {
        List<PurchaseItemDTO> items = purchase.getPurchaseItems().stream()
            .map(item -> {
                GameDTO gameDTO = GameMapper.toGameDTO(item.getGame());
                return new PurchaseItemDTO(
                    item.getId(),
                    item.getGame().getId(),
                    item.getGame().getName(),
                    item.getGame().getPrice(),
                    gameDTO
                );
            }).collect(Collectors.toList());

        return new PurchaseDTO(
            purchase.getId(),
            purchase.getPurchaseDate(),
            purchase.getTotalValue(),
            items
        );
    }
}