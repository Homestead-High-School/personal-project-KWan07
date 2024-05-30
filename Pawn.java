public class Pawn extends Piece{

    public Pawn(boolean color) {
        super(color);
    }

    @Override
    public boolean canMove(Board board, int startX, int startY, int endX, int endY, int kingX, int kingY) {
        if (board.getPiece(endX, endY).getColor() == color){
            return false;
        }
        Piece temp = board.getPiece(endX, endY);
        board.setPiece(endX, endY, this);
        board.setPiece(startX, startY, null);
        if (board.isUnderAttack(kingX, kingY, color)){
            board.setPiece(endX, endY, temp);
            board.setPiece(startX, startY, this);
            return false;
        }
        board.setPiece(endX, endY, temp);
        board.setPiece(startX, startY, this);
        int x = Math.abs(startX - endX);
        int y = endY - startY;
        if (board.getPiece(endX, endY) == null){
            if (hasMoved){
                return x == 0 && y == 1;
            }
            return x == 0 && (y == 1 || y == 2);
        } else {
            return x == 1 && y == 1;
        } 
    }
}
