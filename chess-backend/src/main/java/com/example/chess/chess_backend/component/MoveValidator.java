package com.example.chess.chess_backend.component;

import org.springframework.stereotype.Component;

import com.example.chess.chess_backend.entity.Game;
import com.example.chess.chess_backend.entity.Position;

@Component
public class MoveValidator {

    public boolean isValidMove(String piece, int[] from, int[] to, String color, Game game) {
        Position fromPos = new Position(from[0], from[1]);
        Position toPos = new Position(to[0], to[1]);

        switch (piece.toUpperCase()) {
            case "PAWN":
                return validatePawn(fromPos, toPos, color, game);
            case "ROOK":
                return validateRook(fromPos, toPos);
            case "KNIGHT":
                return validateKnight(fromPos, toPos);
            case "BISHOP":
                return validateBishop(fromPos, toPos);
            case "QUEEN":
                return validateQueen(fromPos, toPos);
            case "KING":
                if (isCastlingMove(fromPos, toPos)) {
                    return validateCastling(fromPos, toPos, game);
                }
                return validateKing(fromPos, toPos);
            default:
                return false;
        }
    }

    private boolean validatePawn(Position from, Position to, String color, Game game) {
        int fromRow = from.getRow(), fromCol = from.getCol();
        int toRow = to.getRow(), toCol = to.getCol();

        boolean isCapture = game.getPieceAt(toRow, toCol) != null &&
                            !game.getPieceAt(toRow, toCol).startsWith(color);

        int dirRow = 0, dirCol = 0, startRow = -1, startCol = -1;

        switch (color.toUpperCase()) {
            case "RED" -> {
                dirRow = -1;
                startRow = 12;
            }
            case "YELLOW" -> {
                dirRow = 1;
                startRow = 1;
            }
            case "BLUE" -> {
                dirCol = 1;
                startCol = 1;
            }
            case "GREEN" -> {
                dirCol = -1;
                startCol = 12;
            }
        }

        // Normal forward move: 1-step
        if (!isCapture) {
            if (dirRow != 0 && fromCol == toCol && toRow - fromRow == dirRow &&
                game.getPieceAt(toRow, toCol) == null) {
                return true;
            }
            if (dirCol != 0 && fromRow == toRow && toCol - fromCol == dirCol &&
                game.getPieceAt(toRow, toCol) == null) {
                return true;
            }

            // First move: 2-step forward
            if (dirRow != 0 && fromCol == toCol &&
                toRow - fromRow == 2 * dirRow && fromRow == startRow &&
                game.getPieceAt(fromRow + dirRow, fromCol) == null &&
                game.getPieceAt(toRow, toCol) == null) {
                return true;
            }
            if (dirCol != 0 && fromRow == toRow &&
                toCol - fromCol == 2 * dirCol && fromCol == startCol &&
                game.getPieceAt(fromRow, fromCol + dirCol) == null &&
                game.getPieceAt(toRow, toCol) == null) {
                return true;
            }
        }

        // Diagonal capture: 1-step diagonally
        if (isCapture) {
            if (dirRow != 0 && Math.abs(toCol - fromCol) == 1 && toRow - fromRow == dirRow) {
                return true;
            }
            if (dirCol != 0 && Math.abs(toRow - fromRow) == 1 && toCol - fromCol == dirCol) {
                return true;
            }
        }

        return false;
    }

    private boolean validateRook(Position from, Position to) {
        return from.getRow() == to.getRow() || from.getCol() == to.getCol();
    }

    private boolean validateKnight(Position from, Position to) {
        int dRow = Math.abs(from.getRow() - to.getRow());
        int dCol = Math.abs(from.getCol() - to.getCol());
        return (dRow == 2 && dCol == 1) || (dRow == 1 && dCol == 2);
    }

    private boolean validateBishop(Position from, Position to) {
        return Math.abs(from.getRow() - to.getRow()) == Math.abs(from.getCol() - to.getCol());
    }

    private boolean validateQueen(Position from, Position to) {
        return validateRook(from, to) || validateBishop(from, to);
    }

    private boolean validateKing(Position from, Position to) {
        return Math.abs(from.getRow() - to.getRow()) <= 1 &&
               Math.abs(from.getCol() - to.getCol()) <= 1;
    }

    private boolean isCastlingMove(Position from, Position to) {
        int dRow = Math.abs(from.getRow() - to.getRow());
        int dCol = Math.abs(from.getCol() - to.getCol());
        return (dRow == 0 && dCol == 2) || (dCol == 0 && dRow == 2);
    }

    private boolean validateCastling(Position from, Position to, Game game) {
        int rookRow = from.getRow();
        int rookCol = from.getCol();

        if (from.getRow() == to.getRow()) {
            rookCol = (to.getCol() > from.getCol()) ? 13 : 0;
        } else if (from.getCol() == to.getCol()) {
            rookRow = (to.getRow() > from.getRow()) ? 13 : 0;
        }

        Position rookPos = new Position(rookRow, rookCol);

        return !game.hasKingMoved(from) &&
               !game.hasRookMoved(from, rookPos) &&
               game.isPathClear(from, rookPos) &&
               !game.isSquareUnderAttack(from) &&
               !game.isSquareUnderAttack(game.getIntermediateKingSquare(from, to)) &&
               !game.isSquareUnderAttack(to);
    }
}
