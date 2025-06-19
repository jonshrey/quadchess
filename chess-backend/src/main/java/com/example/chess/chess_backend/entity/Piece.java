package com.example.chess.chess_backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Piece {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Position position;

    private String type;
    private String color;

    @ManyToOne
    @JoinColumn(name = "player_id")
    @JsonBackReference
    private Player player;

    public Piece() {}

    public Piece(String type, String color, Position position) {
        this.type = type;
        this.color = color;
        this.position = position;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }
    public void setPosition(Position position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }

    // Stub: You should implement the actual move logic for each piece type
    public boolean canMoveTo(Position from, Position to, Game game) {
        // For now, always return false (not implemented)
        return false;
    }
}
