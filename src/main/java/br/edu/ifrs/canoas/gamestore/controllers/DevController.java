package br.edu.ifrs.canoas.gamestore.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrs.canoas.gamestore.dto.DevDTO;
import br.edu.ifrs.canoas.gamestore.model.domain.Dev;
import br.edu.ifrs.canoas.gamestore.service.DevService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/dev")
@CrossOrigin(origins = "*")
public class DevController {
    @Autowired
    private DevService devService ;
    
    @GetMapping("/getAll")
    public ResponseEntity<List<DevDTO>> getAllDev() {
        return ResponseEntity.ok(devService.getAllDev());
    }

    @GetMapping("/getOne/{name}")
    public ResponseEntity<Dev> getOne(@PathVariable String name) {
        Dev dev = devService.getOne(name);
        if (dev == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(dev);
        }
    }
    
    @PostMapping
    public ResponseEntity<Dev> createDev(@RequestBody Dev dev) {
        Dev savedDev = devService.createDev(dev);
        return ResponseEntity.ok(savedDev);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        devService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dev> updateDev(@PathVariable Long id, @RequestBody Dev updatedDev) {
        Dev dev = devService.updateDev(id, updatedDev);
        return ResponseEntity.ok(dev);
    }
}