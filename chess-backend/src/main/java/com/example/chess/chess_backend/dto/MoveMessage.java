package com.example.chess.chess_backend.dto;

public class MoveMessage {
    private Long gameId;
    private Long playerId;
    private String from;
    private String to;
    private String piece;

    // Getters and setters
    public Long getGameId() { return gameId; }
    public void setGameId(Long gameId) { this.gameId = gameId; }

    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public String getPiece() { return piece; }
    public void setPiece(String piece) { this.piece = piece; }
}

