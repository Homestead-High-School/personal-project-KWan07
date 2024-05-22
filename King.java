public class King extends Piece {

    private boolean inCheck;
    
    public King(boolean color){
        super(color);
        inCheck = false;
    }
}
