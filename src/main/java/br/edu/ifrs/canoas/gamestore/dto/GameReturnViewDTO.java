package br.edu.ifrs.canoas.gamestore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameReturnViewDTO {
    private String name;
    private int returns;
    private Integer soldQuantity;
}
