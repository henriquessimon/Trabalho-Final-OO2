package br.edu.ifrs.canoas.gamestore.dto;

import br.edu.ifrs.canoas.gamestore.model.domain.Game;

public class GamePurchaseMapper {
    
    public static GamePurchaseDTO toGamePurchaseDTO(Game game) {
        return new GamePurchaseDTO(
            game.getName(),
            game.getPrice()
        );
    }
}