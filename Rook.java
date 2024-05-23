public class Rook extends Piece{
    private boolean hasMoved; //For castling
    public Rook(boolean color){
        super(color);
    }
    public boolean getHasMoved(){
        return hasMoved;
    }
}
