package br.edu.ifrs.canoas.gamestore.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifrs.canoas.gamestore.dto.GameMapper;
import br.edu.ifrs.canoas.gamestore.dto.GamePurchaseDTO;
import br.edu.ifrs.canoas.gamestore.dto.GameReturnDTO;
import br.edu.ifrs.canoas.gamestore.dto.GameSelectDevolveDTO;
import br.edu.ifrs.canoas.gamestore.dto.PurchaseDTO;
import br.edu.ifrs.canoas.gamestore.dto.PurchaseHistoryDTO;
import br.edu.ifrs.canoas.gamestore.dto.PurchaseItemDTO;
import br.edu.ifrs.canoas.gamestore.dto.PurchaseMapper;
import br.edu.ifrs.canoas.gamestore.model.domain.Game;
import br.edu.ifrs.canoas.gamestore.model.domain.Purchase;
import br.edu.ifrs.canoas.gamestore.service.PurchaseItemService;
import br.edu.ifrs.canoas.gamestore.service.PurchaseService;
import jakarta.persistence.EntityNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private PurchaseItemService purchaseItemService;
    
    @GetMapping("/pedido")
    public ResponseEntity<PurchaseDTO> getPurchase() {
        Purchase purchase = purchaseService.getOrCreateCart();
        PurchaseDTO dto = PurchaseMapper.toDTO(purchase);
        return ResponseEntity.ok(dto);
    }



    private PurchaseDTO convertToDto(Purchase purchase) {
        PurchaseDTO dto = new PurchaseDTO();
        dto.setId(purchase.getId());
        dto.setPurchaseDate(purchase.getPurchaseDate());
        dto.setTotalValue(purchase.getTotalValue());

        List<PurchaseItemDTO> itemsDto = purchase.getPurchaseItems().stream()
            .map(item -> {
                PurchaseItemDTO itemDto = new PurchaseItemDTO();
                Game game = item.getGame();

                itemDto.setId(item.getId());
                itemDto.setGameId(game.getId());
                itemDto.setGameName(game.getName());
                itemDto.setGamePrice(game.getPrice());

                // 💡 Aqui está o ponto que estava faltando
                itemDto.setGame(GameMapper.toGameDTO(game));

                return itemDto;
            }).toList();

        dto.setItems(itemsDto);
        return dto;
    }


    
    @GetMapping("/history")
    public ResponseEntity<List<PurchaseHistoryDTO>> getPurchaseHistory() {
        List<PurchaseHistoryDTO> history = purchaseItemService.getCompletedPurchaseHistory();
        return ResponseEntity.ok(history);
    }

    @GetMapping("/cart/guest")
    public ResponseEntity<PurchaseDTO> getGuestCart() {
        Purchase cart = purchaseService.getGuestCart();
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }
        PurchaseDTO dto = convertToDto(cart);
        return ResponseEntity.ok(dto);
    }


    @PostMapping("/add")
    public ResponseEntity<Purchase> addGameToCart(@RequestBody GamePurchaseDTO gameDTO) {
        try {
            Purchase updatedCart = purchaseService.addGameToCart(gameDTO);
            return ResponseEntity.ok(updatedCart);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Purchase> updatePurchase(@RequestBody Map<String, Object> payload) {
        Long purchaseId = Long.valueOf(payload.get("purchaseId").toString());
        Long userId = Long.valueOf(payload.get("userId").toString());
        Boolean completed = Boolean.valueOf(payload.getOrDefault("completed", "true").toString());

        Purchase updatedPurchase = purchaseService.updatePurchase(purchaseId, userId, completed);

        return ResponseEntity.ok(updatedPurchase);
    }

    @PutMapping("/return")
    public ResponseEntity<String> returnGames(@RequestBody GameReturnDTO returnRequest) {
        try {
            purchaseService.returnGames(returnRequest.getPurchaseId(), returnRequest.getGameIds());
            return ResponseEntity.ok("Jogos devolvidos com sucesso.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar devolução.");
        }
    }
    
    @GetMapping("/{purchaseId}/games")
    public ResponseEntity<List<GameSelectDevolveDTO>> getGamesByPurchase(@PathVariable Long purchaseId) {
        Purchase purchase = purchaseService.findById(purchaseId);
        if (purchase == null) {
            return ResponseEntity.notFound().build();
        }

        List<GameSelectDevolveDTO> games = purchase.getPurchaseItems().stream()
            .map(item -> new GameSelectDevolveDTO(item.getGame().getId(), item.getGame().getName()))
            .toList();

        return ResponseEntity.ok(games);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Purchase> removeGameFromCart(@RequestParam Long gameId) {
        try {
            Purchase updatedCart = purchaseService.removeGameFromCart(gameId);
            return ResponseEntity.ok(updatedCart);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}