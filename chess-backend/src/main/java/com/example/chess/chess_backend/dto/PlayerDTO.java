package com.example.chess.chess_backend.dto;

public class PlayerDTO {
    private Long id;
    private String name;
    private String color;
    private Long score;
    private boolean eliminated;

    public PlayerDTO(Long id, String name, String color, Long score , boolean eliminated) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.score = score;
        this.eliminated = eliminated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
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

    public boolean isEliminated() {
        return eliminated;
    }

    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }

    
}