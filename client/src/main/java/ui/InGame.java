package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import ui.webSocket.WebSocketFacade;

import java.util.Arrays;

public class InGame {
    public static InGameStates state;
    public static String port;

    public static String eval(String port, String input, WebSocketFacade socket) {
        InGame.port = port;
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redrawBoard();
                case "leave" -> leave(socket);
                case "makemove" -> makeMove(socket, input);
                case "resign" -> resign(socket);
                case "legalMoves" -> highlightLegalMoves(input);
                case "quit" -> "quit";
                default -> Repl.help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    public static String redrawBoard(){
        GenerateBoard.generateBoard(ChessGame.TeamColor.WHITE);
        GenerateBoard.generateBoard(ChessGame.TeamColor.BLACK);
        return "board drawn";
    }

    public static String leave(WebSocketFacade socket){
        //if observing delete user from both hashmaps and go back to the logged in menu
        Repl.state = State.SIGNEDIN;
        socket.leave(Repl.auth.authToken(),Repl.gameID);
        return "left";
    }
    public static String makeMove(WebSocketFacade socket, String... params){
        //only for players not observers
        socket.makeMove(Repl.auth.authToken(),Repl.gameID,null);
        //allows user to input what move they want to make
        //board updates, and notifies everyone involved in the game
        return "made move";
    }
    public static String resign(WebSocketFacade socket){
        //confirms user wants to resign, if yes they lose and game is over
        socket.resign(Repl.auth.authToken(), Repl.gameID);
        return "resigned";
        //notify everyone of other player winning
        //doesn't make resigned user leave
    }
    public static String highlightLegalMoves(String...params){
        //allows user to input what piece they want to know moves for
        //current piece's square and possible squares are highlighted
//        if(params.length > 1){
//            String  = params[0];
//        }

        return "highlighted moves";
    }

}
