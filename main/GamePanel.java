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

    public static final int WIDTH = 1100;
    public static final int HEIGHT = 800;
    final int FPS = 60;
    Thread gameThread;
    Board board = new Board();
    Mouse mouse = new Mouse();

    public static ArrayList<Piece> pieces = new ArrayList<>();
    public static ArrayList<Piece> simPieces = new ArrayList<>();
    ArrayList<Piece> promotionPieces = new ArrayList<>();
    Piece activePiece;
    private boolean promotion;

    public static final int WHITE = 0;
    public static final int BLACK = 1;
    int currentColor = WHITE;

    private boolean canMove;
    private boolean validSquare;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        setPieces();
        copyPieces(pieces, simPieces);
        addMouseMotionListener(mouse);
        addMouseListener(mouse);
    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    public void setPieces() {
        pieces.add(new Pawn(WHITE, 0, 6));
        pieces.add(new Pawn(WHITE, 1, 6));
        pieces.add(new Pawn(WHITE, 2, 6));
        pieces.add(new Pawn(WHITE, 3, 6));
        pieces.add(new Pawn(WHITE, 4, 6));
        pieces.add(new Pawn(WHITE, 5, 6));
        pieces.add(new Pawn(WHITE, 6, 6));
        pieces.add(new Pawn(WHITE, 7, 6));
        pieces.add(new Rook(WHITE, 0, 7));
        //pieces.add(new Knight(WHITE, 1, 7));
        //pieces.add(new Bishop(WHITE, 2, 7));
        //pieces.add(new Queen(WHITE, 3, 7));
        pieces.add(new King(WHITE, 4, 7));
       // pieces.add(new Bishop(WHITE, 5, 7));
       // pieces.add(new Knight(WHITE, 6, 7));
        pieces.add(new Rook(WHITE, 7, 7));
        
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
    private void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target) {
        target.clear();
        for (Piece p : source) {
            target.add(p);
        }
    }

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

    private void update() {
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
    private boolean inCheck() {
        for (Piece p : simPieces) {
            if (p )
        }
    }
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
            if (!illegalMove(activePiece)) {
                validSquare = true;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        board.draw(g2);
        for (Piece p : simPieces) {
            p.draw(g2);
        }
        if (activePiece != null) {
            if (canMove) {
                if (illegalMove(activePiece)) {
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

    
}
//canmove and validsquare same?
