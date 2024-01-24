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

    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition, ChessPiece current) {
            Collection<ChessMove> total = new ArrayList<>();
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            ChessGame.TeamColor origColor = current.getTeamColor();
            // up
            int upRow = row;
            while (true) {
                upRow ++;
                ChessPosition posPosition = new ChessPosition(upRow, col);
        //            verify possible position;
        //                out of bounds;
                int posRow = posPosition.getRow();
                int posCol = posPosition.getColumn();
                if (posCol > 8 | posRow > 8) {
                    break;
                }
        //                piece in new position;
                ChessPiece posPiece = board.getPiece(posPosition);
                if (posPiece == null) {
                    total.add(new ChessMove(myPosition, posPosition, null));
                }
                else {
                    ChessGame.TeamColor posColor = posPiece.getTeamColor();
                    if (origColor != posColor) {
                        total.add(new ChessMove(myPosition, posPosition, null));
                        break;
                    } else {
                        break;
                    }
                }

            }
        int downRow = row;
        while (true){
            downRow--;
            ChessPosition posPosition = new ChessPosition(downRow, col);
            int posRow = posPosition.getRow();
            int posCol = posPosition.getColumn();
            if(posCol < 1 || posRow < 1){
                break;
            }
            ChessPiece posPiece = board.getPiece(posPosition);
            if (posPiece == null) {
                total.add(new ChessMove(myPosition, posPosition, null));
            }
            else {
                ChessGame.TeamColor posColor = posPiece.getTeamColor();
                if (origColor != posColor) {
                    total.add(new ChessMove(myPosition, posPosition, null));
                    break;
                } else {
                    break;
                }
            }
        }
        int leftcol = col;
        while(true){
            leftcol--;
            ChessPosition posPosition = new ChessPosition(row, leftcol);
            int posRow = posPosition.getRow();
            int posCol = posPosition.getColumn();
            if(posCol < 1 || posRow < 1){
                break;
            }
            ChessPiece posPiece = board.getPiece(posPosition);
            if (posPiece == null) {
                total.add(new ChessMove(myPosition, posPosition, null));
            }
            else {
                ChessGame.TeamColor posColor = posPiece.getTeamColor();
                if (origColor != posColor) {
                    total.add(new ChessMove(myPosition, posPosition, null));
                    break;
                } else {
                    break;
                }
            }
        }
        int rightcol = col;
        while(true){
            rightcol++;
            ChessPosition posPosition = new ChessPosition(row, rightcol);
            int posRow = posPosition.getRow();
            int posCol = posPosition.getColumn();
            if(posCol > 8 || posRow > 8){
                break;
            }
            ChessPiece posPiece = board.getPiece(posPosition);
            if (posPiece == null) {
                total.add(new ChessMove(myPosition, posPosition, null));
            }
            else {
                ChessGame.TeamColor posColor = posPiece.getTeamColor();
                if (origColor != posColor) {
                    total.add(new ChessMove(myPosition, posPosition, null));
                    break;
                } else {
                    break;
                }
            }
        }
        return total;
    }

    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition, ChessPiece current) {
        Collection<ChessMove> total = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessGame.TeamColor origColor = current.getTeamColor();
        //right diagonal
        int upRow = row;
        int upCol = col;
        while (true) {
            upRow++;
            upCol++;
            ChessPosition posPosition = new ChessPosition(upRow, upCol);
            int posRow = posPosition.getRow();
            int posCol = posPosition.getColumn();
            if (posCol > 8 || posRow > 8) {
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
        int downRow = row;
        int downCol = col;
        while (true) {
            downRow--;
            downCol--;
            ChessPosition posPosition = new ChessPosition(downRow, downCol);
            int posRow = posPosition.getRow();
            int posCol = posPosition.getColumn();
            if (posCol < 1 || posRow < 1) {
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
        int uRow = row;
        int dCol = col;
        while (true) {
            uRow++;
            dCol--;
            ChessPosition posPosition = new ChessPosition(uRow, dCol);
            int posRow = posPosition.getRow();
            int posCol = posPosition.getColumn();
            if (posCol < 1 || posRow > 8) {
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
        int dRow = row;
        int uCol = col;
        while (true) {
            dRow--;
            uCol++;
            ChessPosition posPosition = new ChessPosition(dRow, uCol);
            int posRow = posPosition.getRow();
            int posCol = posPosition.getColumn();
            if (posCol > 8 || posRow < 1) {
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
        //right side
        int rrow = row;
        int rcol = col;
        //+1,+2
        rrow++;
        rcol = rcol + 2;
        ChessPosition posPosition = new ChessPosition(rrow, rcol);
        int posRow = posPosition.getRow();
        int posCol = posPosition.getColumn();
        if (posCol <= 8 && posRow <= 8) {
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

        rrow = row;
        rcol = col;
        //+1,-2
        rrow++;
        rcol = rcol - 2;
        posPosition = new ChessPosition(rrow, rcol);
        posRow = posPosition.getRow();
        posCol = posPosition.getColumn();
        if (posCol >= 1 && posRow <= 8) {
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

        rrow = row;
        rcol = col;
        //+2, +1
        rrow = rrow + 2;
        rcol++;
        posPosition = new ChessPosition(rrow, rcol);
        posRow = posPosition.getRow();
        posCol = posPosition.getColumn();
        if (posCol <= 8 && posRow <= 8) {
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


        rrow = row;
        rcol = col;
        // +2, -1
        rrow = rrow + 2;
        rcol--;
        posPosition = new ChessPosition(rrow, rcol);
        posRow = posPosition.getRow();
        posCol = posPosition.getColumn();
        if (posCol >= 1 && posRow <= 8) {
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

        int lrow = row;
        int lcol = col;
        //-1,+2
        lrow--;
        lcol = lcol + 2;
        posPosition = new ChessPosition(lrow, lcol);
        posRow = posPosition.getRow();
        posCol = posPosition.getColumn();
        if (posCol <= 8 && posRow >= 1) {
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

        lrow = row;
        lcol = col;
        // -1,-2
        lrow--;
        lcol = lcol - 2;
        posPosition = new ChessPosition(lrow, lcol);
        posRow = posPosition.getRow();
        posCol = posPosition.getColumn();
        if (posCol >= 1 && posRow >= 1) {
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

        lrow = row;
        lcol = col;
        // -2, +1
        lrow = lrow - 2;
        lcol++;
        posPosition = new ChessPosition(lrow, lcol);
        posRow = posPosition.getRow();
        posCol = posPosition.getColumn();
        if (posCol <= 8 && posRow >= 1) {
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

        lrow = row;
        lcol = col;
        //-2,-1
        lrow = lrow - 2;
        lcol--;
        posPosition = new ChessPosition(lrow, lcol);
        posRow = posPosition.getRow();
        posCol = posPosition.getColumn();
        if (posCol >= 1 && posRow >= 1 ) {
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
        return total;
    }


    public Collection<ChessMove> whitePawn(ChessBoard board, ChessPosition myPosition, ChessPiece current, ChessGame.TeamColor origColor){
        Collection<ChessMove> total = new ArrayList<>();
        //first move
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (row == 1) {
            row++;
            ChessPosition posPosition = new ChessPosition(row, col);
            ChessPiece posPiece = board.getPiece(posPosition);
            if (posPiece == null) {
                total.add(new ChessMove(myPosition, posPosition, null));
                row++;
                posPosition = new ChessPosition(row, col);
                posPiece = board.getPiece(posPosition);
                if(posPiece == null){
                    total.add(new ChessMove(myPosition, posPosition, null));
                }
            }
        }
        else{
            //diagonal
            row++;
            col++;


//            else{
//    do nothing}
//            if forward is not null then you can't do anything
//    can only capture sideways

        }
        //diagonal capture
        //forward move
    }
    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition, ChessPiece current){
        Collection<ChessMove> total = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessGame.TeamColor origColor = current.getTeamColor();
        if(origColor == ChessGame.TeamColor.WHITE){
            return whitePawn(board, myPosition, current);
        }
        else{
            return blackPawn(board, myPosition,current);
        }
    }


    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece cur = board.getPiece(myPosition);
        if (cur.type == PieceType.KING) {
            return kingMoves(board, myPosition, cur);
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

        return null;
    }
}




