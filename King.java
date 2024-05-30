public class King extends Piece {
    
    public King(boolean color){
        super(color);
    }
    public boolean canCastleLeft(Board board){
        if (hasMoved){
            return false;
        }
        if (color){
            if (board.isUnderAttack(0, 4, true)){
                return false;
            }
            Piece p = board.getPiece(0, 0);
            if (!(p instanceof Rook) || p.getHasMoved()){
                return false;
            }
            for (int j = 1; j <= 3; j++){
                if (board.getPiece(0, j) != null || board.isUnderAttack(0, j, true)){
                    return false;
                }
            }
            return true;
        }
        else {
            if (board.isUnderAttack(7, 4, false)){
                return false;
            }
            Piece p = board.getPiece(7, 0);
            if (!(p instanceof Rook) || p.getHasMoved()){
                return false;
            }
            for (int j = 1; j <= 3; j++){
                if (board.getPiece(7, j) != null || board.isUnderAttack(7, j, true)){
                    return false;
                }
            }
            return true;
        }
    }
    public boolean canCastleRight(Board board){
        if (hasMoved){
            return false;
        }
        if (color){
            if (board.isUnderAttack(0, 4, true)){
                return false;
            }
            Piece p = board.getPiece(0, 7);
            if (!(p instanceof Rook) || p.getHasMoved()){
                return false;
            }
            for (int j = 5; j <= 6; j++){
                if (board.getPiece(0, j) != null || board.isUnderAttack(0, j, true)){
                    return false;
                }
            }
            return true;
        }
        else {
            if (board.isUnderAttack(7, 4, false)){
                return false;
            }
            Piece p = board.getPiece(7, 7);
            if (!(p instanceof Rook) || p.getHasMoved()){
                return false;
            }
            for (int j = 5; j <= 6; j++){
                if (board.getPiece(7, j) != null || board.isUnderAttack(7, j, true)){
                    return false;
                }
            }
            return true;
        }
    }
    @Override
    public boolean canMove(Board board, int startX, int startY, int endX, int endY, int kingX, int kingY) {
        return Math.abs(startX - endX) == 1 && Math.abs(startY - endY) == 1 && !board.isUnderAttack(endX, endY, color);
    }
}
    
