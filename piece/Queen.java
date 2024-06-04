package piece;

import main.GamePanel;

public class Queen extends Piece {

    public Queen(int color, int col, int row) {
        super(color, col, row);
        if (color == GamePanel.WHITE) {
            image = getImage("/res/piece/w-queen");
        } else {
            image = getImage("/res/piece/b-queen");
        }
    }
    public boolean canMove(int endCol, int endRow) {
        //Check if target square is in the board
        if (!inBoard(endCol, endRow)) {
            return false;
        }
        //Check if target square is same square
        if (sameSquare(endCol, endRow)) {
            return false;
        }
        //Check if target square has piece of same color on it
        for (Piece p : GamePanel.simPieces) {
            if (p.col == endCol && p.row == endRow && p.color == color && p != this) {
                return false;
            }
        }
        //Check if queen can move to target square and path clear
        return ((endCol == preCol || endRow == preRow) || (Math.abs(endCol - preCol) == Math.abs(endRow - preRow))) && pathClear(endCol, endRow);
    }
    private boolean pathClear(int endCol, int endRow) {
        if (Math.abs(endCol - preCol) == Math.abs(endRow - preRow)) {
            //If moving NW
            int c1 = preCol - 1;
            int r1 = preRow - 1;
            while (c1 > endCol && r1 > endRow) {
                for (Piece p : GamePanel.simPieces) {
                    if (p.col == c1 && p.row == r1) {
                        return false;
                    }
                }
                c1--;
                r1--;
            }
            //If moving NE
            int c2 = preCol + 1;
            int r2 = preRow - 1;
            while (c2 < endCol && r2 > endRow) {
                for (Piece p : GamePanel.simPieces) {
                    if (p.col == c2 && p.row == r2) {
                        return false;
                    }
                }
                c2++;
                r2--;
            }
            //If moving SE
            int c3 = preCol + 1;
            int r3 = preRow + 1;
            while (c3 < endCol && r3 < endRow) {
                for (Piece p : GamePanel.simPieces) {
                    if (p.col == c3 && p.row == r3) {
                        return false;
                    }
                }
                c3++;
                r3++;
            }
            //If moving SW
            int c4 = preCol - 1;
            int r4 = preRow + 1;
            while (c4 > endCol && r4 < endRow) {
                for (Piece p : GamePanel.simPieces) {
                    if (p.col == c4 && p.row == r4) {
                        return false;
                    }
                }
                c4--;
                r4++;
            }
            return true;
        } else {
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
}
