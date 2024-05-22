import java.util.*;

public class Piece {

    private boolean color; //white = true, black = false
    private int type; //King = 0, Queen = 1, Rook = 2, Bishop = 3, Knight = 4, Pawn = 5
    private boolean captured;
    private Set<int[]> available;


    public Piece(boolean color, int type){
        this.color = color;
        this.type = type;
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
    

}
