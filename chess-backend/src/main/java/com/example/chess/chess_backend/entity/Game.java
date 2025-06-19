package com.example.chess.chess_backend.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Game {
    @Id
    @GeneratedValue

    private Long Id;

    private String Status;

    private String currentTurn;

    private String joinCode; 

    private long creationTimestamp; 

    private boolean isPrivateGame; // NEW: Field to mark game as private or public

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Player> players = new ArrayList<>();


    @OneToMany(cascade= CascadeType.ALL)
    @JsonIgnore
    private List<Move> move = new ArrayList<>();


    
    @ElementCollection
    @CollectionTable(name = "board_state", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "row_state")
    private List<String> boardState = new ArrayList<>();
    

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(String currentTurn) {
        this.currentTurn = currentTurn;
    }

    public String getJoinCode() { 
        return joinCode;
    }

    public void setJoinCode(String joinCode) { 
        this.joinCode = joinCode;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    // NEW: Getter and Setter for isPrivateGame
    public boolean getIsPrivateGame() { // Note: for boolean, getter can be `isPrivateGame()` too
        return isPrivateGame;
    }

    public void setIsPrivateGame(boolean isPrivateGame) {
        this.isPrivateGame = isPrivateGame;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = new ArrayList<>(players);
    }

    public List<Move> getMove() {
        return move;
    }

    public void setMove(List<Move> move) {
        this.move = new ArrayList<>(move);
    }

    public List<String> getBoardState() {
        return boardState;
    }

    public void setBoardState(List<String> boardState) {
        this.boardState = new ArrayList<>(boardState);
    }

    public String getPieceAt(int row, int col) {
        List<String> boardState = this.getBoardState();
        if (row < 0 || row >= boardState.size()) return null;
    
        String[] rowArr = boardState.get(row).split(",");
        if (col < 0 || col >= rowArr.length) return null;
    
        String piece = rowArr[col].trim();
        return piece.equalsIgnoreCase("EMPTY") ? null : piece;
    }
    
    // --- Castling and special move helpers for 4-player chess ---
    public boolean hasKingMoved(Position kingPos) {
        for (Move m : move) {
            if (m.getPiece().equalsIgnoreCase("king") &&
                m.getFromRow() == kingPos.getRow() &&
                m.getFromCol() == kingPos.getCol()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasRookMoved(Position kingPos, Position rookPos) {
        for (Move m : move) {
            if (m.getPiece().equalsIgnoreCase("rook") &&
                m.getFromRow() == rookPos.getRow() &&
                m.getFromCol() == rookPos.getCol()) {
                return true;
            }
        }
        return false;
    }

    public boolean isPathClear(Position from, Position to) {
        int dRow = Integer.compare(to.getRow(), from.getRow());
        int dCol = Integer.compare(to.getCol(), from.getCol());
        int row = from.getRow() + dRow;
        int col = from.getCol() + dCol;
        while (row != to.getRow() || col != to.getCol()) {
            if (!isSquareEmpty(row, col)) {
                return false;
            }
            row += dRow;
            col += dCol;
        }
        return true;
    }

    private boolean isSquareEmpty(int row, int col) {
        if (row < 0 || row >= boardState.size()) return false;
        String boardRow = boardState.get(row);
        if (col < 0 || col >= boardRow.length()) return false;
        char c = boardRow.charAt(col);
        return c == '.' || c == ' ';
    }

    public boolean isSquareUnderAttack(Position pos) {
        for (Player player : players) {
            if (!player.getColor().equalsIgnoreCase(currentTurn)) {
                for (Piece piece : player.getPieces()) {
                    Position piecePos = piece.getPosition();
                    if (piecePos == null) continue;
                    if (piece.canMoveTo(piecePos, pos, this)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Position getIntermediateKingSquare(Position from, Position to) {
        int midRow = (from.getRow() + to.getRow()) / 2;
        int midCol = (from.getCol() + to.getCol()) / 2;
        return new Position(midRow, midCol);
    }
}