package chess;

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
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
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


    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition, ChessPiece current) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessGame.TeamColor origColor = current.getTeamColor();
        // up, check null first
        if (row + 1 < 8) {
            ChessPiece pos = board.getPiece(new ChessPosition(row + 1, col));
            // if null break
            ChessGame.TeamColor posColor = pos.getTeamColor();
            if (origColor != posColor) {
                new ChessMove(myPosition, new ChessPosition(row + 1, col), null);
                // append to collection of possible moves
            }
        }
        // down
        if (row - 1 > 0) {
            ChessPiece pos = board.getPiece(new ChessPosition(row - 1, col));
            // if null break
            ChessGame.TeamColor posColor = pos.getTeamColor();
            if (origColor != posColor) {
                new ChessMove(myPosition, new ChessPosition(row - 1, col), null);
                // append to collection of possible moves
            }
        }
        return null;
    }
//if it's going in the return collection it needs to be 1-based not 0-based
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        ChessPiece cur = board.getPiece(myPosition);
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