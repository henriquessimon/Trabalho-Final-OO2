package br.edu.ifrs.canoas.gamestore.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseItemDTO {
    private Long id;
    private Long gameId;
    private String gameName;
    private BigDecimal gamePrice;
    private GameDTO game;
}
