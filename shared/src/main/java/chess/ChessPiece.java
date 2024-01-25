package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */

    public boolean inboundsCheck(int row, int col){
        if(row >= 1 && row <= 8){
            if (col>= 1 && col <= 8){
                return true;
            }
        }
        return false;
    }


    public Collection<ChessMove> kingmoveCheck(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor origColor, ChessPosition posPosition){
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece pos = board.getPiece(posPosition);
        if(pos == null){
             moves.add(new ChessMove(myPosition, posPosition, null));
        }
        else {
            ChessGame.TeamColor posColor = pos.getTeamColor();
            if (origColor != posColor) {
               moves.add(new ChessMove(myPosition, posPosition, null));
            }
        }
        return moves;
    }

    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition, ChessPiece current, int row, int col, ChessGame.TeamColor origColor) {
        Collection<ChessMove> total = new ArrayList<>();
//        int row = myPosition.getRow();
//        int col = myPosition.getColumn();
//        ChessGame.TeamColor origColor = current.getTeamColor();
        // up, check null first
//        if (row + 1 < 8) {
        boolean check = inboundsCheck(row+1, col);
        if(check){
            Collection<ChessMove> up = kingmoveCheck(board, myPosition, origColor, new ChessPosition(row + 1, col));
            total.addAll(up);
        }
        // down
        check = inboundsCheck(row-1,col);
        if (check) {
            Collection<ChessMove> down = kingmoveCheck(board, myPosition, origColor, new ChessPosition(row - 1, col));
            total.addAll(down);
        }
        // right diagonal
        check = inboundsCheck(row+1,col+1);
        if (check) {
//            if (col + 1 < 8) {
                Collection<ChessMove> urDiag = kingmoveCheck(board, myPosition, origColor, new ChessPosition(row + 1, col +1));
                total.addAll(urDiag);
//            }
        }
        //upper left diagonal
        check = inboundsCheck(row+1,col-1);
        if (check){
//            if (col - 1 > 0 && col - 1 < 8){
                Collection<ChessMove> ulDiag = kingmoveCheck(board, myPosition, origColor, new ChessPosition(row + 1, col - 1));
                total.addAll(ulDiag);
//            }
        }
        // left
        check = inboundsCheck(row, col-1);
        if (check){
            Collection<ChessMove> left = kingmoveCheck(board, myPosition, origColor, new ChessPosition(row, col - 1));
            total.addAll(left);
        }
        //right
        check = inboundsCheck(row, col+1);
        if(check){
            Collection<ChessMove> right = kingmoveCheck(board, myPosition, origColor, new ChessPosition(row, col + 1));
            total.addAll(right);
        }
        //bt left
        check = inboundsCheck(row-1,col-1);
        if (check){
//            if(col - 1 > 0 && col - 1 < 8){
                Collection<ChessMove> btleft = kingmoveCheck(board, myPosition, origColor, new ChessPosition(row - 1, col - 1));
                total.addAll(btleft);
//            }
        }
        //bt right
        check = inboundsCheck(row-1,col+1);
        if (check){
//            if(col + 1 > 0 && col + 1 < 8){
                Collection<ChessMove> btright = kingmoveCheck(board, myPosition, origColor, new ChessPosition(row - 1, col + 1));
                total.addAll(btright);
//            }
        }

        return total;
    }

    public void rmoveCheck (Collection<ChessMove> total, ChessBoard board, ChessPosition myPosition, int row, int col, ChessGame.TeamColor origColor, int manipRow, int manipCol) {
        int newRow = row;
        int newCol = col;
        while (true) {
            newRow = newRow + manipRow;
            newCol = newCol + manipCol;
            ChessPosition posPosition = new ChessPosition(newRow, newCol);
            boolean check = inboundsCheck(newRow, newCol);
            if (!check) {
                break;
            }
            ChessPiece posPiece = board.getPiece(posPosition);
            if (posPiece == null) {
                total.add(new ChessMove(myPosition, posPosition, null));
            } else {
                ChessGame.TeamColor posColor = posPiece.getTeamColor();
                if (origColor != posColor) {
                    total.add(new ChessMove(myPosition, posPosition, null));
                    break;
                } else {
                    break;
                }
            }

        }
    }

    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition, ChessPiece current) {
        Collection<ChessMove> total = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessGame.TeamColor origColor = current.getTeamColor();
        // up
        int up = 1;
        int down = -1;
        rmoveCheck(total,board,myPosition,row,col,origColor,up,0);
        //down
        rmoveCheck(total,board,myPosition,row,col,origColor,down,0);
        //left
        rmoveCheck(total,board,myPosition,row,col,origColor,0,down);
        //right
        rmoveCheck(total,board,myPosition,row,col,origColor,0,up);
        return total;
    }

    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition, ChessPiece current) {
        Collection<ChessMove> total = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessGame.TeamColor origColor = current.getTeamColor();
        //right diagonal
        int up = 1;
        int down = -1;
        rmoveCheck(total,board,myPosition, row,col,origColor,up,up);
        //left down diagonal
        rmoveCheck(total,board,myPosition,row,col,origColor,down,down);
        //upper left diagonal
        rmoveCheck(total,board,myPosition,row,col,origColor,up,down);
        //lower right diagonal
        rmoveCheck(total,board,myPosition,row,col,origColor,down,up);
        return total;
    }

    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition, ChessPiece current){
        Collection<ChessMove> total = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessGame.TeamColor origColor = current.getTeamColor();
        total.addAll(bishopMoves(board,myPosition,current));
        total.addAll(rookMoves(board,myPosition,current));
        return total;
    }

    public void kmovesCheck(Collection<ChessMove> total, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor origColor, int posRow, int posCol){
        ChessPosition posPosition = new ChessPosition(posRow, posCol);
        boolean check = inboundsCheck(posRow,posCol);
        if (check) {
            ChessPiece posPiece = board.getPiece(posPosition);
            if (posPiece == null) {
                total.add(new ChessMove(myPosition, posPosition, null));
            } else {
                ChessGame.TeamColor posColor = posPiece.getTeamColor();
                if (origColor != posColor) {
                    total.add(new ChessMove(myPosition, posPosition, null));
                }
            }
        }
    }

    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition, ChessPiece current){
        Collection<ChessMove> total = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessGame.TeamColor origColor = current.getTeamColor();
        /*
        up right : row +2 col +1 or row +1 col +2
        up left : row + 2 col -1 or row +1 col -2
        down right: row -1 col +2 or row -2 col +1
        down left: row -1 col -2 or row -2 col -1
         */
        int rrow = row;
        int rcol = col;
        //+1,+2
        rrow++;
        rcol = rcol + 2;
        kmovesCheck(total,board,myPosition,origColor,rrow,rcol);

        rrow = row;
        rcol = col;
        //+1,-2
        rrow++;
        rcol = rcol - 2;
        kmovesCheck(total,board,myPosition,origColor,rrow,rcol);

        rrow = row;
        rcol = col;
        //+2, +1
        rrow = rrow + 2;
        rcol++;
        kmovesCheck(total,board,myPosition,origColor,rrow,rcol);

        rrow = row;
        rcol = col;
        // +2, -1
        rrow = rrow + 2;
        rcol--;
        kmovesCheck(total,board,myPosition,origColor,rrow,rcol);


        int lrow = row;
        int lcol = col;
        //-1,+2
        lrow--;
        lcol = lcol + 2;
        kmovesCheck(total,board,myPosition,origColor,lrow,lcol);


        lrow = row;
        lcol = col;
        // -1,-2
        lrow--;
        lcol = lcol - 2;
        kmovesCheck(total,board,myPosition,origColor,lrow,lcol);


        lrow = row;
        lcol = col;
        // -2, +1
        lrow = lrow - 2;
        lcol++;
        kmovesCheck(total,board,myPosition,origColor,lrow,lcol);


        lrow = row;
        lcol = col;
        //-2,-1
        lrow = lrow - 2;
        lcol--;
        kmovesCheck(total,board,myPosition,origColor,lrow,lcol);

        return total;
    }

    public Collection<ChessMove> diagonalCheck(Collection<ChessMove> total, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor origColor, int forwardRow, int forwardCol, int endRow ){
        boolean check = inboundsCheck(forwardRow,forwardCol);
        if(check) {
            ChessPosition posPosition = new ChessPosition(forwardRow, forwardCol);
            ChessPiece posPiece = board.getPiece(posPosition);
            if (forwardRow == endRow) {
                if (posPiece != null) {
                    ChessGame.TeamColor posColor = posPiece.getTeamColor();
                    if (origColor != posColor) {
                        total.add(new ChessMove(myPosition, posPosition, PieceType.ROOK));
                        total.add(new ChessMove(myPosition, posPosition, PieceType.KNIGHT));
                        total.add(new ChessMove(myPosition, posPosition, PieceType.BISHOP));
                        total.add(new ChessMove(myPosition, posPosition, PieceType.QUEEN));

                    }
                }
            } else {
                if (posPiece != null) {
                    ChessGame.TeamColor posColor = posPiece.getTeamColor();
                    if (origColor != posColor) {
                            total.add(new ChessMove(myPosition, posPosition, null));
                        }
                    }
                }
            }
        return total;
    }

    public void diagonalHelper(Collection<ChessMove>total, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor origColor){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int forwardRow = row;
        int forwardCol = col;
        //diagonal
        if(origColor == ChessGame.TeamColor.WHITE) {
            forwardRow++;
            forwardCol++;
            Collection<ChessMove> rightDiag = diagonalCheck(total, board, myPosition, origColor, forwardRow, forwardCol,8);
            total.addAll(rightDiag);
            forwardCol = col;
            forwardCol--;
            Collection<ChessMove> leftDiag = diagonalCheck(total, board, myPosition, origColor, forwardRow, forwardCol,8);
            total.addAll(leftDiag);
        }
        if(origColor == ChessGame.TeamColor.BLACK){
            forwardRow--;
            forwardCol--;
            Collection<ChessMove> rightDiag = diagonalCheck(total, board, myPosition, origColor, forwardRow, forwardCol,1);
            total.addAll(rightDiag);
            forwardCol = col;
            forwardCol++;
            Collection<ChessMove> leftDiag = diagonalCheck(total, board, myPosition, origColor, forwardRow, forwardCol,1);
            total.addAll(leftDiag);
        }
    }
    public Collection<ChessMove> pawnMovesCheck(Collection<ChessMove> total, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor origColor, int homeRow, int endRow, int advance){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int forwardRow = row;
        // move forward
        forwardRow = forwardRow + advance;
        boolean check = inboundsCheck(forwardRow,col);
        if(check) {
                if (row == homeRow) {
                    ChessPosition posPosition = new ChessPosition(forwardRow, col);
                    ChessPiece posPiece = board.getPiece(posPosition);
                    if (posPiece == null) {
                        total.add(new ChessMove(myPosition, posPosition, null));
                        forwardRow = forwardRow + advance;
                        posPosition = new ChessPosition(forwardRow, col);
                        posPiece = board.getPiece(posPosition);
                        if (posPiece == null) {
                            total.add(new ChessMove(myPosition, posPosition, null));
                        }
                    }
                } else if (forwardRow == endRow) {
                    ChessPosition posPosition = new ChessPosition(forwardRow, col);
                    ChessPiece posPiece = board.getPiece(posPosition);
                    if (posPiece == null) {
                        total.add(new ChessMove(myPosition, posPosition, PieceType.BISHOP));
                        total.add(new ChessMove(myPosition, posPosition, PieceType.KNIGHT));
                        total.add(new ChessMove(myPosition, posPosition, PieceType.ROOK));
                        total.add(new ChessMove(myPosition, posPosition, PieceType.QUEEN));
                    }
                    diagonalHelper(total,board,myPosition,origColor);


                } else {
                    ChessPosition posPosition = new ChessPosition(forwardRow, col);
                    ChessPiece posPiece = board.getPiece(posPosition);
                    if (posPiece == null) {
                        total.add(new ChessMove(myPosition, posPosition, null));
                    }
                    diagonalHelper(total,board,myPosition,origColor);
                }
        }
        return total;
    }


    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition, ChessPiece current){
        Collection<ChessMove> total = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessGame.TeamColor origColor = current.getTeamColor();
        if(origColor == ChessGame.TeamColor.WHITE){
            return pawnMovesCheck(total,board,myPosition,origColor,2,8,1);
        }
        else{
            return pawnMovesCheck(total,board, myPosition,origColor,7,1, -1);
        }
    }


    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece cur = board.getPiece(myPosition);
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessGame.TeamColor origColor = cur.getTeamColor();
        if (cur.type == PieceType.KING) {
            return kingMoves(board, myPosition, cur, row, col, origColor);
        }
        if (cur.type == PieceType.ROOK) {
            return rookMoves(board, myPosition, cur);
        }
        if (cur.type == PieceType.BISHOP) {
            return bishopMoves(board, myPosition, cur);
        }
        if (cur.type == PieceType.QUEEN){
            return queenMoves(board, myPosition, cur);
        }
        if (cur.type == PieceType.KNIGHT){
            return knightMoves(board, myPosition,cur);
        }
        if (cur.type == PieceType.PAWN){
            return pawnMoves(board, myPosition,cur);
        }
        return null;
    }
}




