package piece;

import main.GamePanel;

public class Pawn extends Piece {

    public Pawn(int color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.WHITE) {
            image = getImage("/res/piece/w-pawn");
        } else {
            image = getImage("/res/piece/b-pawn");
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
        if (color == GamePanel.WHITE) {
            if (Math.abs(endCol - preCol) == 1) {
                if (endRow - preRow != -1) {
                    return false;
                }
                for (Piece p : GamePanel.simPieces) {
                    if (p.col == endCol && p.row == endRow && p != this) {
                        return true;
                    }
                }
                //En passant
                if (endRow == 2) {
                    for (Piece p : GamePanel.simPieces) {
                        if (p instanceof Pawn && p.col == endCol && p.row == 3) {
                            if (p.movedTwo) {
                                enPassant = true;
                                return true;
                            }
                        }
                    }
                }
                return false;
            } else if (Math.abs(endCol - preCol) == 0) {
                if (endRow - preRow == -2) {
                    if (preRow != 6) {
                        return false;
                    }
                    for (Piece p : GamePanel.simPieces) {
                        if (p.col == endCol && (p.row == endRow || p.row == endRow + 1) && p != this) {
                            return false;
                        }
                    }
                    return true;
                } else if (endRow - preRow == -1) {
                    for (Piece p : GamePanel.simPieces) {
                        if (p.col == endCol && p.row == endRow && p != this) {
                            return false;
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            if (Math.abs(endCol - preCol) == 1) {
                if (endRow - preRow != 1) {
                    return false;
                }
                for (Piece p : GamePanel.simPieces) {
                    if (p.col == endCol && p.row == endRow && p != this) {
                        return true;
                    }
                }
                //En passant
                if (endRow == 5) {
                    for (Piece p : GamePanel.simPieces) {
                        if (p instanceof Pawn && p.col == endCol && p.row == 4) {
                            if (p.movedTwo) {
                                enPassant = true;
                                return true;
                            }
                        }
                    }
                }
                return false;
            } else if (Math.abs(endCol - preCol) == 0) {
                if (endRow - preRow == 2) {
                    if (preRow != 1) {
                        return false;
                    }
                    for (Piece p : GamePanel.simPieces) {
                        if (p.col == endCol && (p.row == endRow || p.row == endRow - 1) && p != this) {
                            return false;
                        }
                    }
                    return true;
                } else if (endRow - preRow == 1) {
                    for (Piece p : GamePanel.simPieces) {
                        if (p.col == endCol && p.row == endRow && p != this) {
                            return false;
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }
}
