package ui;

import chess.ChessGame;
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
//                case "redraw" -> redrawBoard(input);
                case "leave" -> leave(socket, params);
//                case "makeMove" -> makeMove(socket, input);
                case "resign" -> resign(socket, input);
//                case "legalMoves" -> highlightLegalMoves(input);
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    public static String help() {
        if (Repl.state == State.SIGNEDOUT) {
            System.out.println("""
                    - Register <username password email>
                    - Login <username password>
                    - Quit
                    """);
            return """
                    - Register <username password email>
                    - Login <username password>
                    - Quit
                    """;
        }
        System.out.println("""
                - Create <gameName>
                - Join <PlayerColor gameID>
                - List
                - Logout
                - Clear
                - Quit
                """);
        return """
                - Create <gameName>
                - Join <PlayerColor gameID>
                - List
                - Logout
                - Clear
                - Quit
                """;
    }

    public static void redrawBoard(String input){
        //if observer print from white perspective
        if (state == InGameStates.OBSERVER) {
            GenerateBoard.generateBoard(ChessGame.TeamColor.WHITE);
        }else{
            GenerateBoard.generateBoard(ChessGame.TeamColor.BLACK);
        }
        // if not print from player's perspective
    }

    public static String leave(WebSocketFacade socket, String...params){
        //if observing delete user from both hashmaps and go back to the logged in menu
        int gameID = Integer.parseInt(params[0]);
        socket.leave(Repl.auth.authToken(),gameID);
        Repl.state = State.SIGNEDIN;
        return "left";
    }
    public static void makeMove(WebSocketFacade socket, String... params){
        //only for players not observers
        //allows user to input what move they want to make
        //board updates, and notifies everyone involved in the game
    }
    public static String resign(WebSocketFacade socket,  String...params){
        //confirms user wants to resign, if yes they lose and game is over
        int gameID = Integer.parseInt(params[0]);
        socket.resign(Repl.auth.authToken(), gameID);
        return "resigned";
        //notify everyone of other player winning
        //doesn't make resigned user leave
    }
    public static void highlightLegalMoves(String...params){
        //allows user to input what piece they want to know moves for
        //current piece's square and possible squares are highlighted
        //don't notify other users in the game, for local user only
    }

}
