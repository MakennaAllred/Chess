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
        generateBoard(ChessGame.TeamColor.WHITE);
        System.out.println();
        generateBoard(ChessGame.TeamColor.BLACK);
    }

    public static void generateBoard(ChessGame.TeamColor color){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        if(color == ChessGame.TeamColor.WHITE) {
            ChessGame.TeamColor perspective = ChessGame.TeamColor.WHITE;
            drawHeaders(out, perspective);
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            drawChessBoard(out, board, perspective);
            drawHeaders(out, perspective);
        }
        else{
            ChessGame.TeamColor perspective = ChessGame.TeamColor.BLACK;
            drawHeaders(out,perspective);
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            drawChessBoard(out, board, perspective);
            drawHeaders(out,perspective);
        }
    }

    public static void drawHeaders(PrintStream out, ChessGame.TeamColor perspective){
        setGray(out);
        String[] wHeaders = {"a","b","c","d","e","f","g","h"};
        String[] bHeaders = {"h","g","f","e","d","c","b","a"};
        out.print(EMPTY);
        if(perspective == ChessGame.TeamColor.BLACK) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; boardCol++) {
                printHeaderText(out, bHeaders[boardCol]);
                if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                    out.print(" \u2003".repeat(LINE_WIDTH_IN_CHARS));
                }
                else{
                    out.print("     ");
                }
            }
            setBlack(out);
            out.println();
        }
        else{
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; boardCol++) {
                printHeaderText(out, wHeaders[boardCol]);
                if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                    out.print(" \u2003".repeat(LINE_WIDTH_IN_CHARS));
                }
                else{
                    out.print("     ");
                }
            }
            setBlack(out);
            out.println();
        }
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
    private static void setGray(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    public static void drawChessBoard(PrintStream out, ChessBoard board, ChessGame.TeamColor perspective) {
        int startRow = (perspective == ChessGame.TeamColor.BLACK) ? 1 : 8;
        int endRow = (perspective == ChessGame.TeamColor.BLACK) ? 8 : 1;
        int rowIncr = (perspective == ChessGame.TeamColor.BLACK) ? 1 : -1;
        for (int squareRow = startRow; perspective == ChessGame.TeamColor.BLACK ? squareRow <= endRow: squareRow >= endRow; squareRow += rowIncr) {
            boolean isWhiteSquare = (squareRow % 2 == 1);
            setGray(out);
            out.print(" " + squareRow + " ");
            for (int boardCol = 1; boardCol < 9; ++boardCol) {
                if (isWhiteSquare) {
                    setWhite(out);
                } else {
                    setBlack(out);
                }
                ChessPiece current = board.getPiece(new ChessPosition(squareRow, boardCol));
                printPiece(out, current);
                setBlack(out);
                isWhiteSquare = !isWhiteSquare;
            }
            setGray(out);
            out.print(" " + squareRow + " ");
            setBlack(out);
            out.println();
            setBlack(out);
        }
    }



    public static void printPiece(PrintStream out, ChessPiece piece){
        if(piece == null){
            out.print(EMPTY);
            return;
        }
        ChessGame.TeamColor teamColor = piece.getTeamColor();
        ChessPiece.PieceType pieceType = piece.getPieceType();
        String pieceSymbol = "";
        String textColor = switch (pieceType) {
            case ROOK -> {
                pieceSymbol = (teamColor == ChessGame.TeamColor.WHITE) ? WHITE_ROOK : BLACK_ROOK;
                yield (teamColor == ChessGame.TeamColor.WHITE) ? SET_TEXT_COLOR_MAGENTA : SET_TEXT_COLOR_BLUE;
            }
            case KNIGHT -> {
                pieceSymbol = (teamColor == ChessGame.TeamColor.WHITE) ? WHITE_KNIGHT : BLACK_KNIGHT;
                yield (teamColor == ChessGame.TeamColor.WHITE) ? SET_TEXT_COLOR_MAGENTA : SET_TEXT_COLOR_BLUE;
            }
            case BISHOP -> {
                pieceSymbol = (teamColor == ChessGame.TeamColor.WHITE) ? WHITE_BISHOP : BLACK_BISHOP;
                yield (teamColor == ChessGame.TeamColor.WHITE) ? SET_TEXT_COLOR_MAGENTA : SET_TEXT_COLOR_BLUE;
            }
            case QUEEN -> {
                pieceSymbol = (teamColor == ChessGame.TeamColor.WHITE) ? WHITE_QUEEN : BLACK_QUEEN;
                yield (teamColor == ChessGame.TeamColor.WHITE) ? SET_TEXT_COLOR_MAGENTA : SET_TEXT_COLOR_BLUE;
            }
            case KING -> {
                pieceSymbol = (teamColor == ChessGame.TeamColor.WHITE) ? WHITE_KING : BLACK_KING;
                yield (teamColor == ChessGame.TeamColor.WHITE) ? SET_TEXT_COLOR_MAGENTA : SET_TEXT_COLOR_BLUE;
            }
            case PAWN -> {
                pieceSymbol = (teamColor == ChessGame.TeamColor.WHITE) ? WHITE_PAWN : BLACK_PAWN;
                yield (teamColor == ChessGame.TeamColor.WHITE) ? SET_TEXT_COLOR_MAGENTA : SET_TEXT_COLOR_BLUE;
            }
            default -> " ";
        };
        out.print(textColor);
        out.print(pieceSymbol);

    }
}
