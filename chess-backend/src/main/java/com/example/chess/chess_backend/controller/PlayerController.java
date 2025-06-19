package com.example.chess.chess_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.chess.chess_backend.entity.Player;
import com.example.chess.chess_backend.service.PlayerService;

@RestController
@RequestMapping("/api/player")
public class PlayerController {
    @Autowired private PlayerService playerService;

    @PostMapping("/join")
    public Player joinGame(@RequestParam Long gameId,
                           @RequestParam String name,
                           @RequestParam String color,
                           @RequestParam String joinCode) { // Added joinCode parameter
            return playerService.joinGame(gameId, name, color, joinCode);
   }
   
   
    
}
