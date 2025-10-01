package br.edu.ifrs.canoas.gamestore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameSelectDevolveDTO {
    private Long id;
    private String name;
}