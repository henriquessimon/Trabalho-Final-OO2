package br.edu.ifrs.canoas.gamestore.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrs.canoas.gamestore.dto.GameReturnViewDTO;
import br.edu.ifrs.canoas.gamestore.model.domain.GameReturn;
import br.edu.ifrs.canoas.gamestore.service.GameReturnService;

@RestController
@RequestMapping("/gameReturn")
@CrossOrigin(origins = "*")
public class GameReturnController {
    
    @Autowired 
    private GameReturnService gameReturnService;

    @GetMapping("/getAll")
    public ResponseEntity<List<GameReturnViewDTO>> getAllReturnViews() {
        return ResponseEntity.ok(gameReturnService.getAllGameReturnViews());
    }

}
