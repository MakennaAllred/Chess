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

    boolean rookMoveCheck(Collection<ChessMove> total, ChessPosition myPosition, ChessGame.TeamColor origColor, ChessBoard board, ChessPosition posPosition) {
        int origSize = total.size();
        ChessPiece pos = board.getPiece(posPosition);
        if (pos == null) {
            total.add(new ChessMove(myPosition, posPosition, null));
        } else {
            ChessGame.TeamColor posColor = pos.getTeamColor();
            if (origColor != posColor) {
                total.add(new ChessMove(myPosition, posPosition, null));
            }
            return origSize != total.size();
        }
        return false;
    }

    public Collection<ChessMove> nullCheck(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor origColor, ChessPosition posPosition){
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

    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition, ChessPiece current) {
        Collection<ChessMove> total = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessGame.TeamColor origColor = current.getTeamColor();
        // up, check null first
        if (row + 1 < 8) {
            Collection<ChessMove> up = nullCheck(board, myPosition, origColor, new ChessPosition(row + 1, col));
            total.addAll(up);
        }
        // down
        if (row - 1 > 0 && row - 1 < 8) {
            Collection<ChessMove> down = nullCheck(board, myPosition, origColor, new ChessPosition(row - 1, col));
            total.addAll(down);
        }
        // right diagonal
        if (row + 1 < 8) {
            if (col + 1 < 8) {
                Collection<ChessMove> urDiag = nullCheck(board, myPosition, origColor, new ChessPosition(row + 1, col +1));
                total.addAll(urDiag);
            }
        }
        //upper left diagonal
        if (row + 1 < 8){
            if (col - 1 > 0 && col - 1 < 8){
                Collection<ChessMove> ulDiag = nullCheck(board, myPosition, origColor, new ChessPosition(row + 1, col - 1));
                total.addAll(ulDiag);
            }
        }
        // left
        if (col -1 > 0 && col +1 < 8){
            Collection<ChessMove> left = nullCheck(board, myPosition, origColor, new ChessPosition(row, col - 1));
            total.addAll(left);
        }
        //right
        if(col + 1 < 8 ){
            Collection<ChessMove> right = nullCheck(board, myPosition, origColor, new ChessPosition(row, col + 1));
            total.addAll(right);
        }
        //bt left
        if (row - 1 > 0 && row - 1 < 8){
            if(col - 1 > 0 && col - 1 < 8){
                Collection<ChessMove> btleft = nullCheck(board, myPosition, origColor, new ChessPosition(row - 1, col - 1));
                total.addAll(btleft);
            }
        }
        //bt right
        if (row - 1 > 0 && row -1 < 8){
            if(col + 1 > 0 && col + 1 < 8){
                Collection<ChessMove> btright = nullCheck(board, myPosition, origColor, new ChessPosition(row - 1, col + 1));
                total.addAll(btright);
            }
        }

        return total;
    }

    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition, ChessPiece current){
         //can only move up or down or side to side
        Collection<ChessMove> total = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessGame.TeamColor origColor = current.getTeamColor();
        // up
        boolean up = true;
        while (up) {
            for (int i = 0; i < 7; i++) {
                if(row + i < 8) {
                    up = rookMoveCheck(total, myPosition, origColor, board, new ChessPosition(row + i, col));
                }
                }
            }
        //down
        boolean down = true;
        while(down) {
            for (int i = 7; i > 0; i--) {
                if (row - i > 0) {
                    down = rookMoveCheck(total, myPosition, origColor, board, new ChessPosition(row - i, col));
                }
            }
        }
        //left
        boolean left = true;
        while (left) {
            for (int i = 7; i > 0; i--) {
                if (row - i > 0) {
                    left = rookMoveCheck(total, myPosition, origColor, board, new ChessPosition(row, col - i));
                }
            }
        }
        //right
        boolean right = true;
        while(right){
            for (int i = 0; i < 7; i++) {
                    right = rookMoveCheck(total,myPosition, origColor, board, new ChessPosition(row, col+i));
                }
            }

        return total;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        ChessPiece cur = board.getPiece(myPosition);
        if (cur.type == PieceType.KING){
            return kingMoves(board,myPosition,cur);
        }
        if (cur.type == PieceType.ROOK){
            return rookMoves(board,myPosition,cur);
        }

       return null;
    }
}

/**
 * find piece at passed in position
 * look at possible moves for each piece
 * check if new location is occupied and by what color
 * check if each possible move is in bounds
 * add possible moves to an array for each piece and return it
 */


