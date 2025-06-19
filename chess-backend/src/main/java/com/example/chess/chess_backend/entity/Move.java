package com.example.chess.chess_backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Move {
    @Id
    @GeneratedValue
    private Long id;

    private String fromPosition;
    private String toPosition;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    private String pieceMoved;
    private String pieceCaptured;
    private Long timestamp;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFromPosition() {
        return fromPosition;
    }
    public void setFromPosition(String fromPosition) {
        this.fromPosition = fromPosition;
    }
    public String getToPosition() {
        return toPosition;
    }
    public void setToPosition(String toPosition) {
        this.toPosition = toPosition;
    }
    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }
    public String getPieceMoved() {
        return pieceMoved;
    }
    public void setPieceMoved(String pieceMoved) {
        this.pieceMoved = pieceMoved;
    }
    public String getPieceCaptured() {
        return pieceCaptured;
    }
    public void setPieceCaptured(String pieceCaptured) {
        this.pieceCaptured = pieceCaptured;
    }
    public Long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    // Returns the piece type moved (e.g., "king", "rook", etc.)
    public String getPiece() {
        return pieceMoved;
    }

    // Returns the starting row (0-indexed) of the move
    public int getFromRow() {
        String[] parts = fromPosition.split(",");
        return Integer.parseInt(parts[0]);
    }

// Returns the starting column (0-indexed) of the move
    public int getFromCol() {
        String[] parts = fromPosition.split(",");
        return Integer.parseInt(parts[1]);
    }

    public int getToRow() {
        String[] parts = toPosition.split(",");
        return Integer.parseInt(parts[0]);
    }
    
    public int getToCol() {
        String[] parts = toPosition.split(",");
        return Integer.parseInt(parts[1]);
    }
    
}