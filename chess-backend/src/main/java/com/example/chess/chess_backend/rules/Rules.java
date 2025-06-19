package com.example.chess.chess_backend.rules;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.chess.chess_backend.component.MoveValidator;
import com.example.chess.chess_backend.entity.Game;

@Service
public class Rules {

    @Autowired
    private MoveValidator moveValidator;

    public static final int CHECKMATE_POINTS = 20;
    public static final int SELF_STALEMATE_POINTS = 20;
    public static final int STALEMATE_OPPONENT_POINTS = 10;
    public static final int DOUBLE_CHECK_QUEEN = 1;
    public static final int TRIPLE_CHECK_QUEEN = 5;
    public static final int DOUBLE_CHECK_OTHER = 5;
    public static final int TRIPLE_CHECK_OTHER = 20;

    public static final Map<String, Integer> CAPTURE_POINTS = new HashMap<>();
    static {
        CAPTURE_POINTS.put("PAWN", 1);
        CAPTURE_POINTS.put("KNIGHT", 3);
        CAPTURE_POINTS.put("BISHOP", 5);
        CAPTURE_POINTS.put("ROOK", 5);
        CAPTURE_POINTS.put("QUEEN", 9);
    }

    public static int getCapturePoints(String piece) {
        return CAPTURE_POINTS.getOrDefault(piece.toUpperCase(), 0);
    }

    public boolean isValidMove(String movingPiece, int[] fromPos, int[] toPos, String[][] board, Game game) {
        int fromRow = fromPos[0], fromCol = fromPos[1];
        int toRow = toPos[0], toCol = toPos[1];

        final int BOARD_SIZE = 15;

        if (fromRow < 0 || fromRow >= BOARD_SIZE || fromCol < 0 || fromCol >= BOARD_SIZE ||
            toRow < 0 || toRow >= BOARD_SIZE || toCol < 0 || toCol >= BOARD_SIZE) {
            System.out.println("Rule Check: Move coordinates out of board bounds.");
            return false;
        }

        boolean isFromInCutout = (fromRow <= 3 && fromCol <= 3) ||
                                 (fromRow <= 3 && fromCol >= 11) ||
                                 (fromRow >= 11 && fromCol <= 3) ||
                                 (fromRow >= 11 && fromCol >= 11);
        boolean isToInCutout = (toRow <= 3 && toCol <= 3) ||
                               (toRow <= 3 && toCol >= 11) ||
                               (toRow >= 11 && toCol <= 3) ||
                               (toRow >= 11 && toCol >= 11);
        if (isFromInCutout || isToInCutout) {
            System.out.println("Rule Check: Move involves cutout corner square.");
            return false;
        }

        if (board[fromRow][fromCol] == null || board[fromRow][fromCol].equals("EMPTY")) {
            System.out.println("Rule Check: Cannot move from an empty square.");
            return false;
        }

        String movingPieceColor = movingPiece.split("_")[0];
        String pieceType = movingPiece.split("_")[1];
        String targetPiece = board[toRow][toCol];

        if (targetPiece != null && !targetPiece.equals("EMPTY") && targetPiece.startsWith(movingPieceColor)) {
            System.out.println("Rule Check: Cannot capture own piece.");
            return false;
        }

        boolean pieceRuleValid = moveValidator.isValidMove(pieceType, fromPos, toPos, movingPieceColor, game);

        if (!pieceRuleValid) {
            System.out.println("Rule Check: Move not valid for piece type.");
            return false;
        }

        // In future: check king safety (e.g. move doesn't leave king in check)

        return true;
    }
}
