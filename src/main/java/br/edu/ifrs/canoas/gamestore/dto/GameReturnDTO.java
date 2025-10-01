package br.edu.ifrs.canoas.gamestore.dto;

import java.util.List;

import lombok.Data;

@Data
public class GameReturnDTO {
    private Long purchaseId;
    private List<Long> gameIds;

}
