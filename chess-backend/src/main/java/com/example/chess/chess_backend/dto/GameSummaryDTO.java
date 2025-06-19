package com.example.chess.chess_backend.dto;

import java.util.List;

public class GameSummaryDTO {
    private Long id;
    private String status;
    private String currentTurn;
    private List<PlayerDTO> players;
    private boolean isPrivateGame; // NEW: Add isPrivateGame

    public GameSummaryDTO(Long id, String status, String currentTurn, List<PlayerDTO> players) {
        this.id = id;
        this.status = status;
        this.currentTurn = currentTurn;
        this.players = players;
        this.isPrivateGame = false; // Default or fetched later
    }

    // NEW: Constructor to include isPrivateGame
    public GameSummaryDTO(Long id, String status, String currentTurn, List<PlayerDTO> players, boolean isPrivateGame) {
        this.id = id;
        this.status = status;
        this.currentTurn = currentTurn;
        this.players = players;
        this.isPrivateGame = isPrivateGame;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(String currentTurn) {
        this.currentTurn = currentTurn;
    }

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDTO> players) {
        this.players = players;
    }

    // NEW: Getter and Setter for isPrivateGame
    public boolean getIsPrivateGame() { 
        return isPrivateGame;
    }

    public void setIsPrivateGame(boolean isPrivateGame) {
        this.isPrivateGame = isPrivateGame;
    }
}