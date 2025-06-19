package com.example.chess.chess_backend.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Player {
    @Id
    @GeneratedValue
    private Long Id;

    private String name;
    private String color;
    private Long score = 0L;

    @ManyToOne
    @JoinColumn(name = "game_id")
    @JsonBackReference
    private Game game;

    private boolean eliminated = false;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = jakarta.persistence.FetchType.EAGER)
    private List<Piece> pieces = new ArrayList<>();

    public Player() {}

    // Getters and setters

    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }

    public boolean isEliminated() {
        return eliminated;
    }
    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }

    public Long getId() {
        return Id;
    }
    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public Long getScore() {
        return score;
    }
    public void addScore(Long points) {
        if(score == null) score = 0L;
        score += points;
    }

    public List<Piece> getPieces() {
        return pieces;
    }
    public void setPieces(List<Piece> pieces) {
        this.pieces = pieces;
    }
}
