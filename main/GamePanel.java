package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JPanel;

import piece.Bishop;
import piece.King;
import piece.Knight;
import piece.Pawn;
import piece.Piece;
import piece.Queen;
import piece.Rook;

public class GamePanel extends JPanel implements Runnable {

    //Board, graphics, user interaction
    public static final int WIDTH = 1100;
    public static final int HEIGHT = 800;
    final int FPS = 60;
    Thread gameThread;
    Board board = new Board();
    Mouse mouse = new Mouse();

    //Color
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    int currentColor = WHITE;

    //Pieces
    public static ArrayList<Piece> pieces = new ArrayList<>();
    public static ArrayList<Piece> simPieces = new ArrayList<>();
    ArrayList<Piece> promotionPieces = new ArrayList<>();
    Piece activePiece;

    //Valid move
    private boolean canMove;
    private boolean validSquare;

    //Promotion
    private boolean promotion;

    //Constructor - set up board, pieces, mouse
    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        setPieces();
        copyPieces(pieces, simPieces);
        addMouseMotionListener(mouse);
        addMouseListener(mouse);
    }

    //Start the game
    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    //Set starting positions
    public void setPieces() {
        //White
        pieces.add(new Pawn(WHITE, 0, 6));
        pieces.add(new Pawn(WHITE, 1, 6));
        pieces.add(new Pawn(WHITE, 2, 6));
        pieces.add(new Pawn(WHITE, 3, 6));
        pieces.add(new Pawn(WHITE, 4, 6));
        pieces.add(new Pawn(WHITE, 5, 6));
        pieces.add(new Pawn(WHITE, 6, 6));
        pieces.add(new Pawn(WHITE, 7, 6));
        pieces.add(new Rook(WHITE, 0, 7));
        pieces.add(new Knight(WHITE, 1, 7));
        pieces.add(new Bishop(WHITE, 2, 7));
        pieces.add(new Queen(WHITE, 3, 7));
        pieces.add(new King(WHITE, 4, 7));
        pieces.add(new Bishop(WHITE, 5, 7));
        pieces.add(new Knight(WHITE, 6, 7));
        pieces.add(new Rook(WHITE, 7, 7));
        //Black 
        pieces.add(new Pawn(BLACK, 0, 1));
        pieces.add(new Pawn(BLACK, 1, 1));
        pieces.add(new Pawn(BLACK, 2, 1));
        pieces.add(new Pawn(BLACK, 3, 1));
        pieces.add(new Pawn(BLACK, 4, 1));
        pieces.add(new Pawn(BLACK, 5, 1));
        pieces.add(new Pawn(BLACK, 6, 1));
        pieces.add(new Pawn(BLACK, 7, 1));
        pieces.add(new Rook(BLACK, 0, 0));
        pieces.add(new Knight(BLACK, 1, 0));
        pieces.add(new Bishop(BLACK, 2, 0));
        pieces.add(new Queen(BLACK, 3, 0));
        pieces.add(new King(BLACK, 4, 0));
        pieces.add(new Bishop(BLACK, 5, 0));
        pieces.add(new Knight(BLACK, 6, 0));
        pieces.add(new Rook(BLACK, 7, 0));
    }

    //Draws board and pieces
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        board.draw(g2);
        for (Piece p : simPieces) {
            p.draw(g2);
        }
        if (activePiece != null) {
            if (canMove) {
                if (illegalMove(activePiece) || inCheck()) {
                    g2.setColor(Color.red);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                    g2.fillRect(activePiece.col*Board.SQUARE_SIZE, activePiece.row*Board.SQUARE_SIZE, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
                } else {
                    g2.setColor(Color.white);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                    g2.fillRect(activePiece.col*Board.SQUARE_SIZE, activePiece.row*Board.SQUARE_SIZE, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
                }
            }
            activePiece.draw(g2);
        }
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Book Antique", Font.PLAIN, 40));
        g2.setColor(Color.WHITE);
        if (promotion) {
            g2.drawString("Promote to:", 840, 150);
            for (Piece p : promotionPieces) {
                g2.drawImage(p.image, p.getX(p.col), p.getY(p.row), Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
            } 
        }
    }

    //Refreshes game in real time
    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    //Updates state of game based on user input
    private void update() {
        //Special case for promotion
        if (promotion) {
            promote();
        } else {
            if (mouse.pressed) {
                if (activePiece == null) {
                    for (Piece piece : simPieces) {
                        if (piece.color == currentColor && 
                        piece.col == mouse.x/Board.SQUARE_SIZE && 
                        piece.row == mouse.y/Board.SQUARE_SIZE) {
                            activePiece = piece;
                        }
                    }
                } else {
                    simulate();
                }
            } else {
                if (activePiece != null) {
                    if (validSquare) {
                        copyPieces(simPieces, pieces);
                        activePiece.updatePosition();
                        checkCastling();
                        checkEnPassant();
                        activePiece.moved = true;
                        if (promoting()) {
                            promotion = true;
                        } else {
                            changePlayer();
                        }
                        
                    } else {
                        copyPieces(pieces, simPieces);
                        activePiece.resetPosition();
                        activePiece = null;
                    }
                }
            }
        }
    }

    //Simulates potential move
    private void simulate(){
        canMove = false;
        validSquare = false;
        copyPieces(pieces, simPieces);
        activePiece.x = mouse.x - Board.HALF_SQUARE_SIZE;
        activePiece.y = mouse.y - Board.HALF_SQUARE_SIZE;
        activePiece.col = activePiece.getCol(activePiece.x);
        activePiece.row = activePiece.getRow(activePiece.y);
        if (activePiece.canMove(activePiece.col, activePiece.row)) {
            canMove = true;
            for (int i = 0; i < simPieces.size(); i++) {
                Piece p = simPieces.get(i);
                if (p.col == activePiece.col && p.row == activePiece.row && p != activePiece) {
                    simPieces.remove(i);
                }
            }
            if (!illegalMove(activePiece) && !inCheck()) {
                validSquare = true;
            }
        }
    }

    //Changes players
    private void changePlayer() {
        if (currentColor == WHITE) {
            currentColor = BLACK;
            for (Piece p : pieces) {
                if (p.color == BLACK) {
                    p.movedTwo = false;
                }
            }
        } else {
            currentColor = WHITE;
            for (Piece p : pieces) {
                if (p.color == WHITE) {
                    p.movedTwo = false;
                }
            }
        }
        activePiece = null;
    }

    //Copies pieces from two separate lists
    private void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target) {
        target.clear();
        for (Piece p : source) {
            target.add(p);
        }
    }

    //Update rook position if castling occurs
    private void checkCastling() {
        if (activePiece instanceof King && !activePiece.moved) {
            if (activePiece.color == WHITE) {
                if (activePiece.col == 6) {
                    for (Piece p : simPieces) {
                        if (p.col == 7 && p.row == 7) {
                            p.col = 5;
                            p.updatePosition();
                        }
                    }
                } else if (activePiece.col == 2) {
                    for (Piece p : simPieces) {
                        if (p.col == 0 && p.row == 7) {
                            p.col = 3;
                            p.updatePosition();
                        }
                    }
                }
            } else {
                if (activePiece.col == 6) {
                    for (Piece p : simPieces) {
                        if (p.col == 7 && p.row == 0) {
                            p.col = 5;
                            p.updatePosition();
                        }
                    }
                } else if (activePiece.col == 2) {
                    for (Piece p : simPieces) {
                        if (p.col == 0 && p.row == 0) {
                            p.col = 3;
                            p.updatePosition();
                        }
                    }
                }
            }
        }
    }

    //Removes a pawn if en passant occurs
    private void checkEnPassant() {
        if (activePiece instanceof Pawn && activePiece.enPassant) {
            if (activePiece.color == WHITE) {
                for (int i = 0; i < simPieces.size(); i++) {
                    Piece p = simPieces.get(i);
                    if (p.col == activePiece.col && p.row == 3) {
                        simPieces.remove(i);
                        copyPieces(simPieces, pieces);
                    }
                }
            } else {
                for (int i = 0; i < simPieces.size(); i++) {
                    Piece p = simPieces.get(i);
                    if (p.col == activePiece.col && p.row == 4) {
                        simPieces.remove(i);
                        copyPieces(simPieces, pieces);
                    }
                }
            }
            activePiece.enPassant = false;
        }
    }

    //Detects if promotion is occuring
    private boolean promoting() {
        if (activePiece instanceof Pawn) {
            if ((activePiece.color == WHITE && activePiece.row == 0) || (activePiece.color == BLACK && activePiece.row == 7)) {
                promotionPieces.clear();
                promotionPieces.add(new Queen(currentColor, 9, 2));
                promotionPieces.add(new Rook(currentColor, 9, 3));
                promotionPieces.add(new Bishop(currentColor, 9, 4));
                promotionPieces.add(new Knight(currentColor, 9, 5));
                return true;
            }
        }
        return false;
    }

    //Offers promotion choices
    private void promote() {
        if (mouse.pressed) {
            for (Piece p : promotionPieces) {
                if (p.col == mouse.x/Board.SQUARE_SIZE && p.row == mouse.y/Board.SQUARE_SIZE) {
                    p.col = activePiece.col;
                    p.row = activePiece.row;
                    p.updatePosition();
                    simPieces.add(p);
                    for (int i = 0; i < simPieces.size(); i++) {
                        if (simPieces.get(i) == activePiece) {
                            simPieces.remove(i);
                        }
                    }
                    copyPieces(simPieces, pieces);
                    activePiece = null;
                    promotion = false;
                    changePlayer();
                }
            }
        }
    }

    //Detects if king is in check
    private boolean inCheck() {
        for (Piece p : simPieces) {
            if (p.color == currentColor && p instanceof King) {
                for (Piece p1 : simPieces) {
                    if (p1.color != currentColor && p1.canMove(p.col, p.row)) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }
    
    //Detects if move puts king in check
    private boolean illegalMove(Piece piece) {
        if (piece instanceof King) {
            for (Piece p : simPieces) {
                if (p.color != piece.color && p.canMove(piece.col, piece.row)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /*private boolean checkmate() {
        King k = getKing();
        //Check if king can move
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0)) {
                    if (validMove(k, i, j)) {
                        return false;
                    }
                }
            }
        }
        Piece checkingPiece = null;
        for (Piece p : simPieces) {
            if (p.color != currentColor && p.canMove(k.col, k.row)) {
                checkingPiece = p;
            }
        }
        //Check if can block
        int colDiff = Math.abs(checkingPiece.col - k.col);
        int rowDiff = Math.abs(checkingPiece.row - k.row);
        if (colDiff == 0) {
            if (checkingPiece.row < k.row) {
                for (int row = checkingPiece.row; row < k.row; row++) {
                    for (Piece p : simPieces) {
                        if (p != k && p.color != currentColor && p.canMove(k.col, row)) {
                            return false;
                        }
                    }
                }
            } else {
                for (int row = k.row; row < checkingPiece.row; row++) {
                    for (Piece p : simPieces) {
                        if (p != k && p.color != currentColor && p.canMove(k.col, row)) {
                            return false;
                        }
                    }
                }
            }
        } else if (rowDiff == 0) {
            if (checkingPiece.col < k.col) {
                for (int col = checkingPiece.col; col < k.col; col++) {
                    for (Piece p : simPieces) {
                        if (p != k && p.color != currentColor && p.canMove(col, k.row)) {
                            return false;
                        }
                    }
                }
            } else {
                for (int col = k.col; col < checkingPiece.col; col++) {
                    for (Piece p : simPieces) {
                        if (p != k && p.color != currentColor && p.canMove(col, k.row)) {
                            return false;
                        }
                    }
                }
            }
        } else if (colDiff == rowDiff) {
            if (checkingPiece.col < k.col) {
                if (checkingPiece.row < k.row) {
                    for (int col = checkingPiece.col, row = checkingPiece.row; col < k.col; col++, row++) {
                        for (Piece p : simPieces) {
                            if (p != k && p.color != currentColor && p.canMove(col, row)) {
                                return false;
                            }
                        }
                    }
                } else {
                    for (int col = checkingPiece.col, row = checkingPiece.row; col < k.col; col++, row--) {
                        for (Piece p : simPieces) {
                            if (p != k && p.color != currentColor && p.canMove(col, row)) {
                                return false;
                            }
                        }
                    }
                }
            } else {
                if (checkingPiece.row < k.row) {
                    for (int col = checkingPiece.col, row = checkingPiece.row; col > k.col; col--, row++) {
                        for (Piece p : simPieces) {
                            if (p != k && p.color != currentColor && p.canMove(col, row)) {
                                return false;
                            }
                        }
                    }
                } else {
                    for (int col = checkingPiece.col, row = checkingPiece.row; col > k.col; col--, row--) {
                        for (Piece p : simPieces) {
                            if (p != k && p.color != currentColor && p.canMove(col, row)) {
                                return false;
                            }
                        }
                    }
                }
            }
        } else {
            for (Piece p : simPieces) {
                if (p.color != currentColor && p.canMove(checkingPiece.col, checkingPiece.row)) {
                    return false;
                }
            }
        }
        return true;
    }
    private boolean validMove(Piece p, int i, int j) {
        boolean valid = false;
        p.col += i;
        p.row += j;
        if (p.canMove(p.col, p.row)) {
            for (int a = 0; a < simPieces.size(); a++) {
                Piece p1 = simPieces.get(a);
                if (p1 != p && p1.col == p.col && p1.row == p.row) {
                    simPieces.remove(a);
                }
            }
            if (!illegalMove(p)) {
                valid = true;
            }
            p.resetPosition();
            copyPieces(pieces, simPieces);
        }
        return valid;
    }
    private King getKing() {
        for (Piece p : simPieces) {
            if (p instanceof King && p.color == currentColor) {
                return (King) p;
            }
        }
        return null;
    }
    */
}