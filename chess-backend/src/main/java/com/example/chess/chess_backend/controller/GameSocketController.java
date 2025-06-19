package com.example.chess.chess_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.chess.chess_backend.dto.ApiResponse;
import com.example.chess.chess_backend.dto.MoveMessage;
import com.example.chess.chess_backend.service.MoveService;

@Controller
public class GameSocketController {

@Autowired
    private MoveService moveService;

 @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/move")         // client sends to: /app/move
public void handleMove(MoveMessage moveMessage) {
    ApiResponse response = moveService.registerMove(
        moveMessage.getGameId(),
        moveMessage.getPlayerId(),
        moveMessage.getFrom(),
        moveMessage.getTo(),
        moveMessage.getPiece()
    );

    String topic = "/topic/game/" + moveMessage.getGameId();
    messagingTemplate.convertAndSend(topic, response); // Send full response so frontend can react
}
}
