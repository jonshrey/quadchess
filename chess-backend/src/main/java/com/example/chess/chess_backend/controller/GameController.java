package com.example.chess.chess_backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; // NEW: Import RequestParam
import org.springframework.web.bind.annotation.RestController;

import com.example.chess.chess_backend.dto.GameSummaryDTO;
import com.example.chess.chess_backend.dto.PlayerDTO;
import com.example.chess.chess_backend.entity.Game;
import com.example.chess.chess_backend.service.GameService;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    // Create a new game
    // NEW: Modified to accept isPrivate parameter
    @PostMapping("/create")
    public ResponseEntity<?> createGame(@RequestParam(defaultValue = "false") boolean isPrivate) {
        Game game = gameService.createGame(isPrivate); // Pass isPrivate to service
        return ResponseEntity.ok().body(game);
    }

    // Get full game details by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getGame(@PathVariable Long id) {
        Game game = gameService.getGameById(id);
        if (game != null) {
            return ResponseEntity.ok(game);
        } else {
            return ResponseEntity.status(404).body("{\"message\":\"Game not found\"}");
        }
    }

    // Get only the board state for a game
    @GetMapping("/{gameId}/board")
    public ResponseEntity<?> getBoard(@PathVariable Long gameId) {
        Game game = gameService.getGameById(gameId);
        if (game != null) {
            return ResponseEntity.ok().body(game.getBoardState());
        } else {
            return ResponseEntity.status(404).body("{\"message\":\"Game not found\"}");
        }
    }

    // Start a game by ID
    @PostMapping("/{gameId}/start")
    public ResponseEntity<?> startGame(@PathVariable Long gameId) {
        Game game = gameService.startGame(gameId);
        return ResponseEntity.ok().body(game);
    }

    // Get a summary list of all games
    @GetMapping("/all")
    public ResponseEntity<?> getAllGames() {
        List<Game> games = gameService.getAllGames();

        List<GameSummaryDTO> summaryList = games.stream().map(game -> {
            List<PlayerDTO> playerDTOs = game.getPlayers().stream()
                    .map(player -> new PlayerDTO(
                            player.getId(),
                            player.getName(),
                            player.getColor(),
                            player.getScore(),
                            player.isEliminated()))
                    .collect(Collectors.toList());

            return new GameSummaryDTO(
                    game.getId(),
                    game.getStatus(),
                    game.getCurrentTurn(),
                    playerDTOs,
                    game.getIsPrivateGame() // NEW: Pass isPrivateGame
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok().body(summaryList);
    }
}
