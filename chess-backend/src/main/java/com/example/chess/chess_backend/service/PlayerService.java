package com.example.chess.chess_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.chess.chess_backend.entity.Game;
import com.example.chess.chess_backend.entity.Player;
import com.example.chess.chess_backend.repository.GameRepository;
import com.example.chess.chess_backend.repository.PlayerRepository;

@Service
public class PlayerService {
    @Autowired private PlayerRepository playerRepository;
    @Autowired private GameRepository gameRepository;

    public Player joinGame(Long gameId, String name, String color, String joinCode) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));

        // NEW: Conditional join code validation based on game type
        if (game.getIsPrivateGame()) { // If it's a private game, joinCode is required
            if (joinCode == null || !game.getJoinCode().equals(joinCode)) {
                throw new IllegalArgumentException("Invalid join code for this private game.");
            }
        } else { // If it's a public game, joinCode must be null or empty
            if (joinCode != null && !joinCode.trim().isEmpty()) {
                throw new IllegalArgumentException("Public games do not require a join code.");
            }
        }

        if(game.getPlayers().size() >= 4) throw new IllegalStateException("Only 4 Players can Play ");

        String normalizedColor = color.trim().toUpperCase();
        if (!isValidColor(normalizedColor)) {
            throw new IllegalArgumentException("Invalid color. Allowed colors: RED, BLUE, GREEN, YELLOW");
        }

        boolean colorTaken = game.getPlayers().stream()
            .anyMatch(p -> p.getColor().equalsIgnoreCase(color));
        if (colorTaken) throw new IllegalArgumentException("Color already taken");

        Player player = new Player();
        player.setName(name);
        player.setColor(color);
        player.setGame(game);

        return playerRepository.save(player);
    }

    public Player getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with ID: " + playerId));
    }

    private boolean isValidColor(String color) {
        return color.equals("RED") || color.equals("BLUE") || color.equals("GREEN") || color.equals("YELLOW");
    }
}