package br.edu.ifrs.canoas.gamestore.dto;

import java.math.BigDecimal;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer releaseYear;
    private String trailer;
    private String description;
    private String coverImage;
    private DevDTO dev;
    private Set<PlatformDTO> platformsID;
    private Set<GenreDTO> genresID;
}