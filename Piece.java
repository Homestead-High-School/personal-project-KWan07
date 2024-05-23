import java.util.*;

public abstract class Piece {

    protected boolean color; //white = true, black = false
    private boolean captured;
    private Set<int[]> available;


    public Piece(boolean color){
        this.color = color;
        captured = false;
        available = new HashSet<int[]>();
    }

    public boolean getColor(){
        return color;
    }
    public boolean getCaptured(){
        return captured;
    }
    public boolean getAvailable(){
        return available;
    }
    public void setColor(boolean color){
        this.color = color;
    }
    public void setCaptured(boolean captured){
        this.captured = captured;
    }
    public void setAvailable(Set<int[]> available){
        this.available = available;
    }

    public abstract boolean canMove(Board board, int[] start, int[] end);
}
