public class King extends Piece {

    private boolean inCheck;
    public boolean hasMoved; //For castling
    
    public King(boolean color){
        super(color);
        inCheck = false;
        hasMoved = false;
    }
    public boolean canCastleLeft(Board board){
        if (color){
            return !hasMoved && board.getPiece(0, 0) instanceof Rook && !board.getPiece(0, 0).getHasMoved() && board.getPiece(0, 1) == null && board.getPiece(0, 2) == null && board.getPiece(0, 3) == null;
        }
        return !hasMoved && board.getPiece(7, 0) instanceof Rook && !board.getPiece(7, 0).getHasMoved() && board.getPiece(7, 1) == null && board.getPiece(7, 2) == null && board.getPiece(7, 3) == null;
    }
    public boolean canCastleRight(Board board){
        if (color){
            return !hasMoved && board.getPiece(0, 0) instanceof Rook && !board.getPiece(0, 0).getHasMoved() && board.getPiece(0, 1) == null && board.getPiece(0, 2) == null && board.getPiece(0, 3) == null;
        }
        return !hasMoved && board.getPiece(7, 0) instanceof Rook && !board.getPiece(7, 0).getHasMoved() && board.getPiece(7, 1) == null && board.getPiece(7, 2) == null && board.getPiece(7, 3) == null;
    }
}
