public class Queen extends Piece {

    public Queen(boolean color) {
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
        int y = Math.abs(startY - endY);
        return x == y || x == 0 || y == 0;
    }
    
}
