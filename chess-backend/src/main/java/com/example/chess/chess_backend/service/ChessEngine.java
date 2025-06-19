package com.example.chess.chess_backend.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.chess.chess_backend.entity.Game;
import com.example.chess.chess_backend.entity.Player;
import com.example.chess.chess_backend.rules.Rules;

@Service
public class ChessEngine {
    public static final String EMPTY = "EMPTY";
    public static final String INVALID_MOVE = "INVALID";

    @Autowired
    Rules rules;

    public String[][] initializeBoard() {
        String[][] board = new String[14][14];

        for (int i = 0; i < 14; i++) {
            Arrays.fill(board[i], null);
        }

        // RED (Bottom)
        String[] redBack = {"RED_ROOK", "RED_KNIGHT", "RED_BISHOP", "RED_QUEEN", "RED_KING", "RED_BISHOP", "RED_KNIGHT", "RED_ROOK"};
        for (int i = 0; i < redBack.length; i++) {
            board[13][3 + i] = redBack[i];
            board[12][3 + i] = "RED_PAWN";
        }

        // YELLOW (Top)
        String[] yellowBack = {"YELLOW_ROOK", "YELLOW_KNIGHT", "YELLOW_BISHOP", "YELLOW_QUEEN", "YELLOW_KING", "YELLOW_BISHOP", "YELLOW_KNIGHT", "YELLOW_ROOK"};
        for (int i = 0; i < yellowBack.length; i++) {
            board[0][3 + i] = yellowBack[i];
            board[1][3 + i] = "YELLOW_PAWN";
        }

        // BLUE (Left)
        String[] blueBack = {"BLUE_ROOK", "BLUE_KNIGHT", "BLUE_BISHOP", "BLUE_QUEEN", "BLUE_KING", "BLUE_BISHOP", "BLUE_KNIGHT", "BLUE_ROOK"};
        for (int i = 0; i < blueBack.length; i++) {
            board[3 + i][0] = blueBack[i];
            board[3 + i][1] = "BLUE_PAWN";
        }

        // GREEN (Right)
        String[] greenBack = {"GREEN_ROOK", "GREEN_KNIGHT", "GREEN_BISHOP", "GREEN_QUEEN", "GREEN_KING", "GREEN_BISHOP", "GREEN_KNIGHT", "GREEN_ROOK"};
        for (int i = 0; i < greenBack.length; i++) {
            board[3 + i][13] = greenBack[i];
            board[3 + i][12] = "GREEN_PAWN";
        }

        // Clear corners
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) board[i][j] = null;
            for (int j = 11; j <= 13; j++) board[i][j] = null;
        }
        for (int i = 11; i <= 13; i++) {
            for (int j = 0; j <= 2; j++) board[i][j] = null;
            for (int j = 11; j <= 13; j++) board[i][j] = null;
        }

        return board;
    }

    public List<String> serializeBoard(String[][] board) {
        return Arrays.stream(board)
                .map(row -> Arrays.stream(row)
                        .map(cell -> cell == null ? EMPTY : cell)
                        .collect(Collectors.joining(",")))
                .collect(Collectors.toList());
    }

    public String[][] deserializeBoard(List<String> boardState) {
        String[][] board = new String[14][14];
        for (int i = 0; i < 14; i++) {
            String[] cells = boardState.get(i).split(",", -1);
            for (int j = 0; j < 14; j++) {
                board[i][j] = j < cells.length && !cells[j].equals(EMPTY) ? cells[j] : null;
            }
        }
        return board;
    }
    

    private int[] parsePosition(String pos) {
        String[] p = pos.split(",");
        return new int[]{Integer.parseInt(p[0]), Integer.parseInt(p[1])};
    }

    // ✅ NEW METHOD
    public boolean isMoveValid(Game game, Player player, String from, String to, String piece) {
        int[] fromPos = parsePosition(from);
        int[] toPos = parsePosition(to);
        String[][] board = deserializeBoard(game.getBoardState());

        String movingPiece = board[fromPos[0]][fromPos[1]];

        return movingPiece != null &&
               movingPiece.startsWith(player.getColor()) &&
               movingPiece.equals(piece) &&
               rules.isValidMove(movingPiece, fromPos, toPos, board, game);  // ✅ FIXED ARGUMENT
    }

    public String makeMove(Game game, Player player, String from, String to, String piece) {
        int[] fromPos = parsePosition(from);
        int[] toPos = parsePosition(to);
        int fromRow = fromPos[0], fromCol = fromPos[1];
        int toRow = toPos[0], toCol = toPos[1];

        String[][] board = deserializeBoard(game.getBoardState());

        if (fromRow < 0 || fromRow >= 14 || fromCol < 0 || fromCol >= 14 ||
            toRow < 0 || toRow >= 14 || toCol < 0 || toCol >= 14) {
            System.out.println("[DEBUG] Invalid indices: from=" + from + ", to=" + to);
            return INVALID_MOVE;
        }

        String movingPiece = board[fromRow][fromCol];
        String targetPiece = board[toRow][toCol];

        if (movingPiece == null || movingPiece.equals(EMPTY) ||
            !movingPiece.startsWith(player.getColor()) ||
            !movingPiece.equals(piece)) {
            System.out.println("[DEBUG] Invalid piece match or empty: movingPiece=" + movingPiece + ", expected=" + piece);
            return INVALID_MOVE;
        }

        if (!rules.isValidMove(movingPiece, fromPos, toPos, board, game)) {
            System.out.println("[DEBUG] Move is not valid by rules: " + from + " -> " + to);
            return INVALID_MOVE;
        }

        // Capture
        if (targetPiece != null && !targetPiece.equals(EMPTY)) {
            Long score = (long) Rules.getCapturePoints(targetPiece.split("_")[1]);
            player.addScore(score);
            System.out.println("[DEBUG] Captured piece: " + targetPiece + ", Score added: " + score);
        }

        // Apply move
        board[toRow][toCol] = movingPiece;
        board[fromRow][fromCol] = EMPTY;
        game.setBoardState(serializeBoard(board));

        System.out.println("[DEBUG] Board Row " + fromRow + ": " + game.getBoardState().get(fromRow));
        System.out.println("[DEBUG] Board Row " + toRow + ": " + game.getBoardState().get(toRow));

        return targetPiece == null || targetPiece.equals(EMPTY) ? null : targetPiece;
    }

    public String getNextTurn(Game game) {
        List<String> turnOrder = Arrays.asList("RED", "GREEN", "YELLOW", "BLUE");
        int index = turnOrder.indexOf(game.getCurrentTurn());
        if (index == -1) index = 0;

        List<String> activeColors = game.getPlayers().stream()
                .filter(p -> !p.isEliminated())
                .map(Player::getColor)
                .toList();

        int initialIndex = index;
        do {
            index = (index + 1) % turnOrder.size();
            if (index == initialIndex && !activeColors.contains(turnOrder.get(index))) return null;
        } while (!activeColors.contains(turnOrder.get(index)));

        return turnOrder.get(index);
    }
}
