public class Board {
    private Piece[][] board;
    public Board(){
        board = new Piece[8][8];
        board[0][0] = new Rook(true);
        board[0][1] = new Knight(true);
        board[0][2] = new Bishop(true);
        board[0][3] = new Queen(true);
        board[0][4] = new King(true);
        board[0][5] = new Bishop(true);
        board[0][6] = new Knight(true);
        board[0][7] = new Rook(true);
        for (int j = 0; j < 8; j++){
            board[1][j] = new Pawn(true);
        }
        board[7][0] = new Rook(false);
        board[7][1] = new Knight(false);
        board[7][2] = new Bishop(false);
        board[7][3] = new Queen(false);
        board[7][4] = new King(false);
        board[7][5] = new Bishop(false);
        board[7][6] = new Knight(false);
        board[7][7] = new Rook(false);
        for (int j = 0; j < 8; j++){
            board[6][j] = new Pawn(false);
        }
    }
    public Piece getPiece(int x, int y){
        return board[x][y];
    }
    public void setPiece(int x, int y, Piece piece){
        board[x][y] = piece;
    }
    public boolean isUnderAttack(int x, int y, boolean color){
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (board[i][j] != null && board[i][j].getColor() != color && board[i][j].canMove(this, i, j, x, y)){
                    return true;
                }
            }
        }
        return false;
    }
    

}
