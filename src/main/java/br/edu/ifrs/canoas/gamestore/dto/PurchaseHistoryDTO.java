package br.edu.ifrs.canoas.gamestore.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseHistoryDTO {
    private Long purchaseId;
    private LocalDateTime purchaseDate;
    private BigDecimal totalValue;
    private List<GameDTO> games;
}