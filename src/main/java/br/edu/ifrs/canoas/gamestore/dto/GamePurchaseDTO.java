package br.edu.ifrs.canoas.gamestore.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GamePurchaseDTO {
    private String name;
    private BigDecimal price;
}
