package ui;

import java.util.Arrays;

public class InGame {
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

    }

}
