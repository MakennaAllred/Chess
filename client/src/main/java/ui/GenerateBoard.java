package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class GenerateBoard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int LINE_WIDTH_IN_CHARS = 1;

    public static void main (String[] args){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawHeaders(out);
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        drawChessBoard(out, board);


    }

    public static void drawHeaders(PrintStream out){
        setBlack(out);
        String[] headers = {"a","b","c","d","e","f","g","h"};
        for(int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; boardCol++){
            printHeaderText(out, headers[boardCol]);
            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
            }
            setBlack(out);
        }
        out.println();
    }

    public static void printHeaderText(PrintStream out, String s){
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
        out.print(s);
    }

    public static void setBlack(PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }
    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    public static void drawChessBoard(PrintStream out, ChessBoard board){
        for (int squareRow = 1; squareRow < 9; ++squareRow){
            boolean isWhiteSquare = (squareRow % 2 == 0);
            for(int boardCol = 1; boardCol < 9; ++boardCol){
                if(isWhiteSquare) {
                    setWhite(out);
                }
                else{
                    setBlack(out);
                }
                ChessPiece current = board.getPiece(new ChessPosition(squareRow,boardCol));
                printPiece(out,current);
                setBlack(out);
                isWhiteSquare = !isWhiteSquare;
            }

            out.println();
            setBlack(out);
        }
    }
    public static void printPiece(PrintStream out, ChessPiece piece){
        if(piece == null){
            out.print(EMPTY);
        }
        else {
            ChessGame.TeamColor col = piece.getTeamColor();
            ChessPiece.PieceType type = piece.getPieceType();
            if (col == ChessGame.TeamColor.WHITE) {
                if (type == ChessPiece.PieceType.ROOK) {
                    out.print(SET_TEXT_COLOR_MAGENTA);
                    out.print(WHITE_ROOK);
                }
                if (type == ChessPiece.PieceType.BISHOP) {
                    out.print(SET_TEXT_COLOR_MAGENTA);
                    out.print(WHITE_BISHOP);
                }
                if (type == ChessPiece.PieceType.KNIGHT) {
                    out.print(SET_TEXT_COLOR_MAGENTA);
                    out.print(WHITE_KNIGHT);
                }
                if (type == ChessPiece.PieceType.QUEEN) {
                    out.print(SET_TEXT_COLOR_MAGENTA);
                    out.print(WHITE_QUEEN);
                }
                if (type == ChessPiece.PieceType.KING) {
                    out.print(SET_TEXT_COLOR_MAGENTA);
                    out.print(WHITE_KING);
                }
                if (type == ChessPiece.PieceType.PAWN) {
                    out.print(SET_TEXT_COLOR_MAGENTA);
                    out.print(WHITE_PAWN);
                }
            } else {
                if (type == ChessPiece.PieceType.ROOK) {
                    out.print(SET_TEXT_COLOR_BLUE);
                    out.print(BLACK_ROOK);
                }
                if (type == ChessPiece.PieceType.BISHOP) {
                    out.print(SET_TEXT_COLOR_BLUE);
                    out.print(BLACK_BISHOP);
                }
                if (type == ChessPiece.PieceType.KNIGHT) {
                    out.print(SET_TEXT_COLOR_BLUE);
                    out.print(BLACK_KNIGHT);
                }
                if (type == ChessPiece.PieceType.QUEEN) {
                    out.print(SET_TEXT_COLOR_BLUE);
                    out.print(BLACK_QUEEN);
                }
                if (type == ChessPiece.PieceType.KING) {
                    out.print(SET_TEXT_COLOR_BLUE);
                    out.print(BLACK_KING);
                }
                if (type == ChessPiece.PieceType.PAWN) {
                    out.print(SET_TEXT_COLOR_BLUE);
                    out.print(WHITE_PAWN);
                }
            }
        }
    }
}
