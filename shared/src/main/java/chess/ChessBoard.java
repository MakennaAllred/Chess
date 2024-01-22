package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] board = new ChessPiece[8][8];

    public ChessBoard() {
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = position.getRow() - 1;
        int col = position.getColumn() - 1;
        board[row][col] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = position.getRow() - 1;
        int col = position.getColumn() - 1;
        boolean b = board[row][col] == null;
        if (b) {
            return null;
        } else {
            return board[row][col];
        }

    }


    @Override
    public String toString() {
        return "ChessBoard{" +
                "board=" + Arrays.toString(board) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            board[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            board[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);

            if (i == 0) {
                board[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
                board[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
            }
            if (i == 1) {
                board[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
                board[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);

            }
            if (i == 2) {
                board[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
                board[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
            }
            if (i == 3) {
                board[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
                board[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);

            }
            if (i == 4) {
                board[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
                board[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);

            }
            if (i == 5) {
                board[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
                board[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
            }
            if (i == 6) {
                board[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
                board[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
            }

            if (i == 7) {
                board[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
                board[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
            }

        }
    }

}




//        for(int i = 0; i < 8; i ++){
//            for(int j = 0; j < 8; j++) {
//                //royals
//                boolean wroyals = i == 0;
//                boolean broyals = i == 7;
//                boolean rook = j == 0 | j == 7;
//                boolean knight = j == 1 | j == 6;
//                boolean bishop = j == 2 | j == 5;
//                boolean queen = j == 3;
//                boolean king = j == 4;
//
//                if (wroyals) {
//                    if (rook) {
//                        board[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
//                    }
//                    if (knight) {
//                        board[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
//                    }
//                    if (bishop) {
//                        board[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
//                    }
//                    if (queen) {
//                        board[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
//                    }
//                    if (king) {
//                        board[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
//                    }
//                }
//                if (broyals) {
//                    if (rook) {
//                        board[i][j] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
//                    }
//                    if (knight) {
//                        board[i][j] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
//                    }
//                    if (bishop) {
//                        board[i][j] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
//                    }
//                    if (queen) {
//                        board[i][j] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
//                    }
//                    if (king) {
//                        board[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
//                    }
//                }
//
//                //pawns
//                boolean wpaw = i == 1;
//                boolean bpaw = i == 6;
//                if (wpaw) {
//                    board[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//                }
//                if (bpaw) {
//                    board[i][j] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
//                }
//            }
//
//        }

//iterate through pawns and then hard code special characters
//create to String to debug

