package piece;

import main.GamePanel;

public class King extends Piece {

    public King(int color, int col, int row) {
        super(color, col, row);
        if (color == GamePanel.WHITE) {
            image = getImage("/res/piece/w-king");
        } else {
            image = getImage("/res/piece/b-king");
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
        //Check if king can move to target square
        if (Math.abs(endCol - preCol) <= 1 && Math.abs(endRow - preRow) <= 1) {
            return true;
        }
        //Check if king can castle
        if (moved) {
            return false;
        }
        if (!pathClear(endCol, endRow)) {
            return false;
        }
        if (color == GamePanel.WHITE) {
            if (endCol == 6 && endRow == 7) {
                for (Piece p : GamePanel.simPieces) {
                    if (p.col == 7 && p.row == 7 && !p.moved) {
                        return true;
                    } 
                }
            } else if (endCol == 2 && endRow == 7) {
                for (Piece p : GamePanel.simPieces) {
                    if (p.col == 1 && p.row == 7) {
                        return false;
                    }
                }
                for (Piece p : GamePanel.simPieces) {
                    if (p.col == 0 && p.row == 7 && !p.moved) {
                        return true;
                    } 
                }
            }
        } else {
            if (endCol == 6 && endRow == 0) {
                for (Piece p : GamePanel.simPieces) {
                    if (p.col == 7 && p.row == 0 && !p.moved) {
                        return true;
                    } 
                }
            } else if (endCol == 2 && endRow == 0) {
                for (Piece p : GamePanel.simPieces) {
                    if (p.col == 1 && p.row == 0) {
                        return false;
                    }
                }
                for (Piece p : GamePanel.simPieces) {
                    if (p.col == 0 && p.row == 0 && !p.moved) {
                        return true;
                    } 
                }
            }
        }
        return false;
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
        return true;
    }
    
}
