package piece;

import main.GamePanel;

public class Rook extends Piece {

    public Rook(int color, int col, int row) {
        super(color, col, row);
        if (color == GamePanel.WHITE) {
            image = getImage("/res/piece/w-rook");
        } else {
            image = getImage("/res/piece/b-rook");
        }
    }
    public boolean canMove(int endCol, int endRow) {
        //Check if target square is in the board
        if (!inBoard(endCol, endRow)) {
            return false;
        }
        //Check if target square has piece of same color on it
        for (Piece p : GamePanel.simPieces) {
            if (p.col == endCol && p.row == endRow && p.color == color && p != this) {
                return false;
            }
        }
        //Check if path is clear
        if (!pathClear(endCol, endRow)) {
            return false;
        }
        //Check if rook can move to target square
        return (endCol == preCol) ^ (endRow == preRow);
    }
    private boolean pathClear(int endCol, int endRow) {
        //If moving left
        for (int c = preCol - 1; c > endCol; c--) {
            for (Piece p : GamePanel.simPieces) {
                if (p.col == c && p.row == endRow) {
                    return false;
                }
            }
        }
        //If moving right
        for (int c = preCol + 1; c < endCol; c++) {
            for (Piece p : GamePanel.simPieces) {
                if (p.col == c && p.row == endRow) {
                    return false;
                }
            }
        }
        //If moving up
        for (int r = preRow - 1; r > endRow; r--) {
            for (Piece p : GamePanel.simPieces) {
                if (p.col == endCol && p.row == r) {
                    return false;
                }
            }
        }
        //If moving down
        for (int r = preRow + 1; r < endRow; r++) {
            for (Piece p : GamePanel.simPieces) {
                if (p.col == endCol && p.row == r) {
                    return false;
                }
            }
        }
        return true;
    }
}
