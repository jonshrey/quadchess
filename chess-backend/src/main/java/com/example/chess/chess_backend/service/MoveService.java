package com.example.chess.chess_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.chess.chess_backend.dto.ApiResponse;
import com.example.chess.chess_backend.entity.Game;
import com.example.chess.chess_backend.entity.Move;
import com.example.chess.chess_backend.entity.Player;
import com.example.chess.chess_backend.repository.GameRepository;
import com.example.chess.chess_backend.repository.MoveRepository;
import com.example.chess.chess_backend.repository.PlayerRepository;

@Service
public class MoveService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private MoveRepository moveRepository;

    @Autowired
    private ChessEngine chessEngine;

    @Transactional
    public ApiResponse registerMove(Long gameId, Long playerId, String from, String to, String piece) {
        Game game = gameRepository.findById(gameId).orElse(null);
        Player player = playerRepository.findById(playerId).orElse(null);

        if (game == null || player == null) {
            return new ApiResponse(false, "Game or Player not found", null);
        }

        // Validate the move first
        boolean valid = chessEngine.isMoveValid(game, player, from, to, piece);
        if (!valid) {
            return new ApiResponse(false, "Invalid move", null);
        }

        // ✅ Apply the move (only after validation passes)
        String capturedPiece = chessEngine.makeMove(game, player, from, to, piece);

        // 🔍 Debug print: updated board state
        System.out.println("✅ Updated Board State:");
        List<String> updatedBoard = game.getBoardState();
        for (int i = 0; i < updatedBoard.size(); i++) {
            System.out.printf("Row %2d: %s%n", i, updatedBoard.get(i));
        }

        // Update the current turn
        String nextTurn = chessEngine.getNextTurn(game);
        game.setCurrentTurn(nextTurn);

        // Persist game and player
        gameRepository.save(game);
        playerRepository.save(player);

        // Save move
        Move move = new Move();
        move.setGame(game);
        move.setPlayer(player);
        move.setFromPosition(from);
        move.setToPosition(to);
        move.setPieceMoved(piece);
        move.setPieceCaptured(capturedPiece);
        move.setTimestamp(System.currentTimeMillis());

        Move savedMove = moveRepository.save(move);
        return new ApiResponse(true, "Move successful", savedMove);
    }
}
