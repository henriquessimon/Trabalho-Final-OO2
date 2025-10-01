package br.edu.ifrs.canoas.gamestore.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import br.edu.ifrs.canoas.gamestore.model.domain.Platform;
import br.edu.ifrs.canoas.gamestore.service.PlatformService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/platform")
@CrossOrigin(origins = "*")
public class PlatformController {

    @Autowired
    private PlatformService platformService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Platform>> getAllPlatforms() {
        return ResponseEntity.ok(platformService.getAllPlatforms());
    }

    @GetMapping("/getOne/{name}")
    public ResponseEntity<Platform> getOne(@PathVariable String name) {
        Platform platform = platformService.getOne(name);
        if (platform == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(platform);
    }

    @PostMapping
    public ResponseEntity<Platform> createPlatform(@RequestBody Platform platform) {
        Platform savedPlatform = platformService.createPlatform(platform);
        return ResponseEntity.ok(savedPlatform);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        platformService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Platform> updatePlatform(@PathVariable Long id, @RequestBody Platform updatedPlatform) {
        Platform platform = platformService.updatePlatform(id, updatedPlatform);
        return ResponseEntity.ok(platform);
    }

}