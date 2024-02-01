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
    private ChessPiece.PieceType type;
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
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }


    public boolean inboundsCheck(int row, int col){
        if(row >= 1 && row <= 8){
            if(col >=1 && col <= 8){
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> total = new ArrayList<>();
        ChessPiece original = board.getPiece(myPosition);
        ChessGame.TeamColor origColor = original.getTeamColor();
        PieceType cur = original.type;
        if(cur == PieceType.KING){
            kingMoves(total,board,myPosition,origColor);
        }
        if(cur == PieceType.ROOK){
            rookMoves(total,board,myPosition,origColor);
        }
        if(cur == PieceType.BISHOP){
            bishopMoves(total,board,myPosition,origColor);
        }
        if(cur == PieceType.QUEEN){
            queenMoves(total,board,myPosition,origColor);
        }
        if(cur == PieceType.KNIGHT){
            knightMoves(total,board,myPosition,origColor);
        }
        if(cur == PieceType.PAWN){
            if(origColor == ChessGame.TeamColor.WHITE){
                pawnMoves(total,board,myPosition,origColor,1,2,8);
            }
            else{
                pawnMoves(total,board,myPosition,origColor,-1,7,1);
            }
        }
        return total;
    }

    public void kingMoves(Collection<ChessMove> total, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor origColor) {
        //up
        kingMovesCheck(total,board,myPosition,origColor,1,0);
        //down
        kingMovesCheck(total,board,myPosition,origColor,-1,0);
        // right
        kingMovesCheck(total,board,myPosition,origColor,0,1);
        //left
        kingMovesCheck(total,board,myPosition,origColor,0,-1);
        //uprightdiagonal
        kingMovesCheck(total,board,myPosition,origColor,1,1);
        //downrightdiagonal
        kingMovesCheck(total,board,myPosition,origColor,-1,1);
        //upleftdiagonal
        kingMovesCheck(total,board,myPosition,origColor,1,-1);
        //downleftdiagonal
        kingMovesCheck(total,board,myPosition,origColor,-1,-1);
    }

    public void kingMovesCheck(Collection<ChessMove> total,ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor origColor, int rowManip, int colManip){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        row = row + rowManip;
        col = col + colManip;
        boolean check = inboundsCheck(row,col);
        if(check){
            ChessPosition posPosition = new ChessPosition(row,col);
            ChessPiece posPiece = board.getPiece(posPosition);
            if(posPiece == null){
                total.add(new ChessMove(myPosition,posPosition,null));
            }
            else{
                ChessGame.TeamColor posColor = posPiece.getTeamColor();
                if(origColor != posColor){
                    total.add(new ChessMove(myPosition,posPosition,null));
                }
            }
        }
    }
    public void rookMoves(Collection<ChessMove> total, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor origColor) {
        //up
        rookMovesCheck(total,board,myPosition,origColor,1,0);
        //down
        rookMovesCheck(total,board,myPosition,origColor,-1,0);
        // right
        rookMovesCheck(total,board,myPosition,origColor,0,1);
        //left
        rookMovesCheck(total,board,myPosition,origColor,0,-1);
    }
    public void rookMovesCheck(Collection<ChessMove> total,ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor origColor, int rowManip, int colManip){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        while(true){
            row = row + rowManip;
            col = col + colManip;
            boolean check = inboundsCheck(row,col);
            if(!check){
                break;
            }
            ChessPosition posPosition = new ChessPosition(row,col);
            ChessPiece posPiece = board.getPiece(posPosition);
            if(posPiece == null){
                total.add(new ChessMove(myPosition,posPosition,null));
            }
            else{
                ChessGame.TeamColor posColor = posPiece.getTeamColor();
                if(origColor != posColor){
                    total.add(new ChessMove(myPosition,posPosition,null));
                    break;
                }
                else{
                    break;
                }
            }
        }
    }
    public void bishopMoves(Collection<ChessMove> total, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor origColor) {
        //upright
        rookMovesCheck(total,board,myPosition,origColor,1,1);
        //downright
        rookMovesCheck(total,board,myPosition,origColor,-1,1);
        // downleft
        rookMovesCheck(total,board,myPosition,origColor,-1,-1);
        //upleft
        rookMovesCheck(total,board,myPosition,origColor,1,-1);
    }
    public void queenMoves(Collection<ChessMove> total, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor origColor) {
        rookMoves(total,board,myPosition,origColor);
        bishopMoves(total,board,myPosition,origColor);
    }

    public void knightMoves(Collection<ChessMove> total, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor origColor) {
        //upright: (+1,+2) (+2,+1)
        kingMovesCheck(total,board,myPosition,origColor,1,2);
        kingMovesCheck(total,board,myPosition,origColor,2,1);
        //downright: (-1,+2) (-2,+1)
        kingMovesCheck(total,board,myPosition,origColor,-1,2);
        kingMovesCheck(total,board,myPosition,origColor,-2,1);
        //upleft: (+1,-2) (+2,-1)
        kingMovesCheck(total,board,myPosition,origColor,1,-2);
        kingMovesCheck(total,board,myPosition,origColor,2,-1);
        //downleft:(-1,-2) (-2,-1)
        kingMovesCheck(total,board,myPosition,origColor,-1,-2);
        kingMovesCheck(total,board,myPosition,origColor,-2,-1);
    }

    public void pawnMoves(Collection<ChessMove> total,ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor origColor, int advance, int homeRow, int endRow){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int forwardRow = row;
        forwardRow = forwardRow + advance;
        boolean check = inboundsCheck(forwardRow,col);
        if(check){
            ChessPosition posPosition = new ChessPosition(forwardRow,col);
            ChessPiece posPiece = board.getPiece(posPosition);
            if(posPiece == null){
                if(row == homeRow) {
                    total.add(new ChessMove(myPosition, posPosition, null));
                    forwardRow = forwardRow + advance;
                    posPosition = new ChessPosition(forwardRow,col);
                    posPiece = board.getPiece(posPosition);
                    if(posPiece == null){
                        total.add(new ChessMove(myPosition,posPosition,null));
                    }
                    if(origColor == ChessGame.TeamColor.WHITE){
                        pawnMovesCheck(total,board,myPosition,origColor,1,1,false);
                        pawnMovesCheck(total,board,myPosition,origColor,1,-1,false);
                    }
                    else{
                        pawnMovesCheck(total,board,myPosition,origColor,-1,-1,false);
                        pawnMovesCheck(total,board,myPosition,origColor,-1,1,false);
                    }
                }
                else if(forwardRow == endRow){
                    total.add(new ChessMove(myPosition,posPosition,PieceType.ROOK));
                    total.add(new ChessMove(myPosition,posPosition,PieceType.BISHOP));
                    total.add(new ChessMove(myPosition,posPosition,PieceType.KNIGHT));
                    total.add(new ChessMove(myPosition,posPosition,PieceType.QUEEN));
                    if(origColor == ChessGame.TeamColor.WHITE){
                        pawnMovesCheck(total,board,myPosition,origColor,1,1,true);
                        pawnMovesCheck(total,board,myPosition,origColor,1,-1,true);
                    }
                    else{
                        pawnMovesCheck(total,board,myPosition,origColor,-1,-1,true);
                        pawnMovesCheck(total,board,myPosition,origColor,-1,1,true);
                    }
                }
                else{
                    total.add(new ChessMove(myPosition,posPosition,null));
                    if(origColor == ChessGame.TeamColor.WHITE){
                        pawnMovesCheck(total,board,myPosition,origColor,1,1,false);
                        pawnMovesCheck(total,board,myPosition,origColor,1,-1,false);
                    }
                    else{
                        pawnMovesCheck(total,board,myPosition,origColor,-1,-1,false);
                        pawnMovesCheck(total,board,myPosition,origColor,-1,1,false);
                    }
                }
            }
            else{
                if(origColor == ChessGame.TeamColor.WHITE){
                    pawnMovesCheck(total,board,myPosition,origColor,1,1,false);
                    pawnMovesCheck(total,board,myPosition,origColor,1,-1,false);
                }
                else{
                    pawnMovesCheck(total,board,myPosition,origColor,-1,-1,false);
                    pawnMovesCheck(total,board,myPosition,origColor,-1,1,false);
                }
            }
        }

    }

    public void pawnMovesCheck(Collection<ChessMove> total,ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor origColor, int rowManip, int colManip, boolean promorow) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        row = row + rowManip;
        col = col + colManip;
        boolean check = inboundsCheck(row, col);
        if (check) {
            ChessPosition posPosition = new ChessPosition(row, col);
            ChessPiece posPiece = board.getPiece(posPosition);
            if (posPiece != null) {
                ChessGame.TeamColor posColor = posPiece.getTeamColor();
                if (origColor != posColor) {
                    if (promorow) {
                        total.add(new ChessMove(myPosition, posPosition, PieceType.ROOK));
                        total.add(new ChessMove(myPosition, posPosition, PieceType.KNIGHT));
                        total.add(new ChessMove(myPosition, posPosition, PieceType.QUEEN));
                        total.add(new ChessMove(myPosition, posPosition, PieceType.BISHOP));
                    } else {
                        total.add(new ChessMove(myPosition, posPosition, null));
                    }
                }
            }
        }
    }







}