package ui;

import chess.ChessBoard;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class GenerateBoard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 1;
    private static final int LINE_WIDTH_IN_CHARS = 1;
    private static final String EMPTY = "   ";

    public static void main (String[] args){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawHeaders(out);
        drawChessBoard(out);
        out.print(new ChessBoard());


    }

    public static void drawHeaders(PrintStream out){
        setBlack(out);
        String[] headers = {" a "," b "," c "," d "," e "," f "," g "," h "};
        for(int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; boardCol++){
            drawHeader(out, headers[boardCol]);
        }
        out.println();
    }
    public static void drawHeader(PrintStream out, String header){
        printHeaderText(out, header);
    }

    public static void printHeaderText(PrintStream out, String s){
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLUE);
        out.print(s);
        setBlack(out);
    }

    public static void setBlack(PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }
    private static void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_BLACK);
    }
    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setBlue(PrintStream out) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_WHITE);
    }
    public static void drawChessBoard(PrintStream out){
        for(int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; boardRow++){
            drawRowofSquares(out);
        }
        setBlack(out);
    }
    public static void drawRowofSquares(PrintStream out){
        for (int squareRow = 0; squareRow < 8; ++squareRow){
            boolean isWhiteSquare = (squareRow % 2 == 0);
            for(int boardCol = 0; boardCol < 8; ++boardCol){
                if(isWhiteSquare) {
                    setWhite(out);
                }
                else{
                    setBlack(out);
                }
                if(isWhiteSquare) {
                    out.print(ROOK);
                }
                setBlack(out);
            }

            out.println();
        }
    }
}
