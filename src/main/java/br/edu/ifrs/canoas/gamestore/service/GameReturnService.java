package br.edu.ifrs.canoas.gamestore.service;

import br.edu.ifrs.canoas.gamestore.dto.GameReturnViewDTO;
import br.edu.ifrs.canoas.gamestore.model.domain.Game;
import br.edu.ifrs.canoas.gamestore.model.domain.GameReturn;
import br.edu.ifrs.canoas.gamestore.model.domain.Purchase;
import br.edu.ifrs.canoas.gamestore.model.repository.GameReturnRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GameReturnService {

    @Autowired
    private GameReturnRepository gameReturnRepository;

    @Autowired
    private GameService gameService;

    public List<GameReturnViewDTO> getAllGameReturnViews() {
        List<GameReturn> gameReturns = gameReturnRepository.findAll();

        // Mapear os jogos únicos devolvidos
        Map<Long, Game> uniqueGames = new HashMap<>();
        for (GameReturn gr : gameReturns) {
            Game game = gr.getGame();
            uniqueGames.putIfAbsent(game.getId(), game);
        }

        return uniqueGames.values().stream()
            .map(game -> new GameReturnViewDTO(
                game.getName(),
                game.getReturnCount() != null ? game.getReturnCount() : 0,
                game.getSoldQuantity()
            ))
            .collect(Collectors.toList());
    }


    public void returnGames(Purchase purchase, List<Long> gameIds) {
        boolean anyReturned = false;

        for (Long gameId : gameIds) {
            boolean alreadyReturned = gameReturnRepository.existsByGame_IdAndPurchase_Id(gameId, purchase.getId());
            if (alreadyReturned) {
                continue;
            }

            Game game = gameService.getGameById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Jogo com ID " + gameId + " não encontrado"));

            GameReturn gameReturn = new GameReturn();
            gameReturn.setGame(game);
            gameReturn.setPurchase(purchase);
            gameReturnRepository.save(gameReturn);

            int currentReturns = game.getReturnCount() != null ? game.getReturnCount() : 0;
            game.setReturnCount(currentReturns + 1);
            gameService.createGame(game);

            anyReturned = true;
        }

        if (!anyReturned) {
            throw new IllegalStateException("Todos os jogos selecionados já foram devolvidos.");
        }
    }
}
