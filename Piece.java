public abstract class Piece {

    protected boolean color; //white = true, black = false
    protected boolean captured;
    protected boolean hasMoved;

    public Piece(boolean color){
        this.color = color;
        captured = false;
        hasMoved = false;
    }

    public boolean getColor(){
        return color;
    }
    public boolean getCaptured(){
        return captured;
    }
    public boolean getHasMoved(){
        return hasMoved;
    }
    public void setColor(boolean color){
        this.color = color;
    }
    public void setCaptured(boolean captured){
        this.captured = captured;
    }
    public void setHasMoved(boolean hasMoved){
        this.hasMoved = hasMoved;
    }
    
    public abstract boolean canMove(Board board, int startX, int startY, int endX, int endY, int kingX, int kingY);
}
