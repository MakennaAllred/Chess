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

    public Collection<ChessMove> notNull(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor origColor, ChessPosition posPosition){
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
            Collection<ChessMove> up = notNull(board, myPosition, origColor, new ChessPosition(row + 1, col));
            total.addAll(up);
        }
        // down
        if (row - 1 > 0 && row - 1 < 8) {
            Collection<ChessMove> down = notNull(board, myPosition, origColor, new ChessPosition(row - 1, col));
            total.addAll(down);
        }
        // right diagonal
        if (row + 1 < 8) {
            if (col + 1 < 8) {
                Collection<ChessMove> urDiag = notNull(board, myPosition, origColor, new ChessPosition(row + 1, col +1));
                total.addAll(urDiag);
            }
        }
        //upper left diagonal
        if (row + 1 < 8){
            if (col - 1 > 0 && col - 1 < 8){
                Collection<ChessMove> ulDiag = notNull(board, myPosition, origColor, new ChessPosition(row + 1, col - 1));
                total.addAll(ulDiag);
            }
        }
        // left
        if (col -1 > 0 && col +1 < 8){
            Collection<ChessMove> left = notNull(board, myPosition, origColor, new ChessPosition(row, col - 1));
            total.addAll(left);
        }
        //right
        if(col + 1 < 8 ){
            Collection<ChessMove> right = notNull(board, myPosition, origColor, new ChessPosition(row, col + 1));
            total.addAll(right);
        }
        //bt left
        if (row - 1 > 0 && row - 1 < 8){
            if(col - 1 > 0 && col - 1 < 8){
                Collection<ChessMove> btleft = notNull(board, myPosition, origColor, new ChessPosition(row - 1, col - 1));
                total.addAll(btleft);
            }
        }
        //bt right
        if (row - 1 > 0 && row -1 < 8){
            if(col + 1 > 0 && col + 1 < 8){
                Collection<ChessMove> btright = notNull(board, myPosition, origColor, new ChessPosition(row - 1, col + 1));
                total.addAll(btright);
            }
        }

        return total;
    }

//if it's going in the return collection it needs to be 1-based not 0-based
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        ChessPiece cur = board.getPiece(myPosition);
        if (cur.type == PieceType.KING){
            return kingMoves(board,myPosition,cur);
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