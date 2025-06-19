package com.example.chess.chess_backend.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Position {
    private int row;
    private int col;

    public Position() {
        // Default constructor needed by JPA
    }

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }
    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }
    public void setCol(int col) {
        this.col = col;
    }
}
