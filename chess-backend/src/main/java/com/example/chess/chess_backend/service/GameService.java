package com.example.chess.chess_backend.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.chess.chess_backend.entity.Game;
import com.example.chess.chess_backend.repository.GameRepository;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ChessEngine chessEngine;

    private String generateJoinCode() {
        int min = 100000;
        int max = 999999;
        return String.valueOf(ThreadLocalRandom.current().nextInt(min, max + 1));
    }

    // NEW: Modified createGame to accept isPrivate
    public Game createGame(boolean isPrivate) {
        Game game = new Game();
        game.setStatus("Waiting");
        game.setCurrentTurn("RED");
        game.setCreationTimestamp(System.currentTimeMillis());
        game.setIsPrivateGame(isPrivate); // NEW: Set private game flag

        if (isPrivate) {
            game.setJoinCode(generateJoinCode()); // NEW: Generate code only for private games
        } else {
            game.setJoinCode(null); // Public games don't have a join code
        }

        String[][] board = chessEngine.initializeBoard();
        game.setBoardState(chessEngine.serializeBoard(board));

        return gameRepository.save(game);
    }

    // Fetch game by ID (null if not found)
    public Game getGameById(Long gameId) {
        return gameRepository.findById(gameId).orElse(null);
    }

    // Start game by ID
    public Game startGame(Long gameId) {
        Optional<Game> optionalGame = gameRepository.findById(gameId);
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            game.setStatus("In-Progress");
            game.setCurrentTurn("RED");
            return gameRepository.save(game);
        }
        return null;
    }

    // Save game (used for updates)
    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    // Get all games
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }
}