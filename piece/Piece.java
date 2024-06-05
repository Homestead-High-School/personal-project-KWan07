package piece;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Board;

public class Piece {
    
    public BufferedImage image;
    public int x, y;
    public int col, row, preCol, preRow;
    public int color;
    public boolean moved;
    public boolean movedTwo, enPassant; //For en passant

    public Piece(int color, int col, int row) {
        this.color = color;
        this.col = col;
        this.row = row;
        x = getX(col);
        y = getY(row);
        preCol = col;
        preRow = row;
        moved = false;
        movedTwo = false;
        enPassant = false;
    }
    public BufferedImage getImage(String imagePath) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    
    public int getX(int col) {
        return col * Board.SQUARE_SIZE;
    }
    public int getY(int row) {
        return row * Board.SQUARE_SIZE;
    }
    public int getCol(int x) {
        return (x + Board.HALF_SQUARE_SIZE)/Board.SQUARE_SIZE;
    }
    public int getRow(int y) {
        return (y + Board.HALF_SQUARE_SIZE)/Board.SQUARE_SIZE;
    }
    
    public void updatePosition() {
        if (Math.abs(row - preRow) == 2) {
            movedTwo = true;
        }
        x = getX(col);
        y = getY(row);
        preCol = getCol(x);
        preRow = getRow(y);
    }
    public boolean canMove(int endCol, int endRow) {
        return true;
    }
    public boolean sameSquare(int endCol, int endRow) {
        return endCol == preCol && endRow == preRow;
    }
    public void resetPosition() {
        col = preCol;
        row = preRow;
        x = getX(col);
        y = getY(row);
    }
    public boolean inBoard(int col, int row) {
        return (col >= 0 && col <= 7) && (row >= 0 && row <= 7);
    }
    public void draw(Graphics2D g2) {
        g2.drawImage(image, x, y, Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
    }
}
