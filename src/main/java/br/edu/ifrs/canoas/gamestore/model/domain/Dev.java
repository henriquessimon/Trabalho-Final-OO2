package br.edu.ifrs.canoas.gamestore.model.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Dev {
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Id
    private Long id;
    private String name;
    

    @OneToMany(mappedBy = "dev", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Game> games = new ArrayList<>();
}