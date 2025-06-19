package com.example.chess.chess_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.chess.chess_backend.dto.ApiResponse;
import com.example.chess.chess_backend.entity.Move;
import com.example.chess.chess_backend.service.GameService;
import com.example.chess.chess_backend.service.MoveService;
import com.example.chess.chess_backend.service.PlayerService;

@RestController
@RequestMapping("/api/move")
public class MoveController {

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private MoveService moveService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/make")
    public ResponseEntity<ApiResponse> makeMove(
            @RequestParam Long gameId,
            @RequestParam Long playerId,
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam String piece
    ) {
        ApiResponse response = moveService.registerMove(gameId, playerId, from, to, piece);

        // Only broadcast to WebSocket if move is valid and contains a Move object
        if (response.isSuccess() && response.getData() instanceof Move move) {
            messagingTemplate.convertAndSend("/topic/game/" + gameId, move);
        }

        return ResponseEntity.status(200).body(response);
    }
}
