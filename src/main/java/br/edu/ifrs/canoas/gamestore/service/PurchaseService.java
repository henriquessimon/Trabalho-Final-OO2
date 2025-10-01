package br.edu.ifrs.canoas.gamestore.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import br.edu.ifrs.canoas.gamestore.dto.GamePurchaseDTO;
import br.edu.ifrs.canoas.gamestore.dto.GameSelectDevolveDTO;
import br.edu.ifrs.canoas.gamestore.dto.PurchaseDTO;
import br.edu.ifrs.canoas.gamestore.dto.PurchaseItemDTO;
import br.edu.ifrs.canoas.gamestore.model.domain.Game;
import br.edu.ifrs.canoas.gamestore.model.domain.Purchase;
import br.edu.ifrs.canoas.gamestore.model.domain.PurchaseItem;
import br.edu.ifrs.canoas.gamestore.model.domain.User;
import br.edu.ifrs.canoas.gamestore.model.repository.PurchaseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class PurchaseService {
    
    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    @Lazy
    private GameService gameService;

    @Autowired
    public void setGameService(@Lazy GameService gameService) {
        this.gameService = gameService;
    }

    @Autowired
    private UserService userService;

    @Autowired
    private GameReturnService gameReturnService;

    @Autowired
    @Lazy
    private PurchaseItemService purchaseItemService;

    public Purchase findById(Long id) {
        Optional<Purchase> optional = purchaseRepository.findById(id);
        return optional.orElse(null);
    }

    public List<GameSelectDevolveDTO> getGamesFromPurchase(Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId).orElse(null);
        if (purchase == null) return List.of();

        return purchase.getPurchaseItems().stream()
                .map(item -> new GameSelectDevolveDTO(
                        item.getGame().getId(),
                        item.getGame().getName()
                ))
                .collect(Collectors.toList());
    }

    private boolean isGameAlreadyInCart(Purchase purchase, Game game) {
        return purchase.getPurchaseItems().stream()
            .anyMatch(item -> item.getGame().getId().equals(game.getId()));
    }

    public Purchase getOrCreateCart() {
        return purchaseRepository.findFirstByCompletedFalse()
            .orElseGet(() -> {
                Purchase newCart = new Purchase();
                newCart.setCompleted(false);
                newCart.setPurchaseDate(LocalDateTime.now());
                newCart.setTotalValue(BigDecimal.ZERO);
                return purchaseRepository.save(newCart);
            });
    }

    public Purchase addGameToCart(GamePurchaseDTO gameDTO) {
        Purchase purchase = getOrCreateCart();

        Game game = gameService.getGameEntityByName(gameDTO.getName());
        if (game == null) {
            throw new IllegalArgumentException("Jogo não encontrado!");
        }

        if (isGameAlreadyInCart(purchase, game)) {
            throw new IllegalStateException("O jogo já está no carrinho!");
        }

        PurchaseItem item = new PurchaseItem();
        item.setGame(game);
        item.setPurchase(purchase);

        purchase.getPurchaseItems().add(item);
        purchase.setTotalValue(purchase.getTotalValue().add(game.getPrice()));

        return purchaseRepository.save(purchase);
    }

    public Purchase getGuestCart() {
        return purchaseRepository.findByUserIdIsNullAndCompletedFalse();
    }
    
    @Transactional
    public Purchase updatePurchase(Long purchaseId, Long userId, Boolean completed) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
            .orElseThrow(() -> new EntityNotFoundException("Compra não encontrada"));

        User user = userService.getById(userId)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        purchase.setUser(user);
        purchase.setCompleted(completed);
        purchase.setPurchaseDate(LocalDateTime.now());

        // Atualiza soldQuantity de cada jogo
        for (PurchaseItem item : purchase.getPurchaseItems()) {
            Game game = item.getGame();
            Integer atual = game.getSoldQuantity() != null ? game.getSoldQuantity() : 0;
            game.setSoldQuantity(atual + 1);
            gameService.createGame(game); // salva alteração no banco
        }

        return purchaseRepository.save(purchase); // JPA irá propagar alterações nos jogos se gerenciados
    }

    @Transactional
    public void handleGameRemovalFromPurchases(Game game) {
        purchaseItemService.removeItemsByGame(game);
    }

    public void returnGames(Long purchaseId, List<Long> gameIds) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
            .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));

        if (!Boolean.TRUE.equals(purchase.getCompleted())) {
            throw new IllegalStateException("Não é possível devolver um pedido não finalizado.");
        }

        gameReturnService.returnGames(purchase, gameIds);
    }

    @Transactional
    public void recalculateTotal(Purchase purchase) {
        BigDecimal newTotal = purchase.getPurchaseItems().stream()
            .map(pi -> pi.getGame().getPrice())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        purchase.setTotalValue(newTotal);
        purchaseRepository.save(purchase);
    }

    public Purchase removeGameFromCart(Long gameId) {
        Purchase cart = this.getOrCreateCart();

        PurchaseItem toRemove = cart.getPurchaseItems().stream()
            .filter(item -> item.getGame().getId().equals(gameId))
            .findFirst()
            .orElseThrow(() -> new EntityNotFoundException("Game not found in cart"));

        cart.getPurchaseItems().remove(toRemove);
        purchaseItemService.removeItem(toRemove); // delega a responsabilidade
        recalculateTotal(cart);

        return purchaseRepository.save(cart);
    }

}