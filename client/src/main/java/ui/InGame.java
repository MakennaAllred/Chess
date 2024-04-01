package ui;

import chess.ChessGame;

import java.util.Arrays;

public class InGame {
    public static InGameStates state;
    public static int port;
    public static String eval( int port, String input) {
        InGame.port = port;
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redrawBoard(params);
                case "leave" -> leave(params);
                case "makeMove" -> makeMove(params);
                case "resign" -> resign(params);
                case "legalMoves" -> highlightLegalMoves(params);
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
        return null;
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

    public static void redrawBoard(String... params){
        //if observer print from white perspective
        GenerateBoard.generateBoard(ChessGame.TeamColor.WHITE);
        // if not print from player's perspective
    }

    public static void leave(String...params){
        //if observing delete user from both hashmaps and go back to the logged in menu
        //if player removes username color name still goes back to post log in menu
    }
    public static void makeMove(String... params){
        //only for players not observers
        //allows user to input what move they want to make
        //board updates, and notifies everyone involved in the game
    }
    public static void resign(String...params){
        //confirms user wants to resign, if yes they lose and game is over
        //notify everyone of other player winning
        //doesn't make resigned user leave
    }
    public static void highlightLegalMoves(String...params){
        //allows user to input what piece they want to know moves for
        //current piece's square and possible squares are highlighted
        //don't notify other users in the game, for local user only
    }

}
