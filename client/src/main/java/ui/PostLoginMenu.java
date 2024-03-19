package ui;

import dataAccess.customExceptions.DataAccessException;

import java.util.Arrays;

public class PostLoginMenu {

    public void eval(String input){
        try{
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens,1,tokens.length);
            return switch(cmd){
                case "join game" -> joinGame(params);
                case "create game" -> createGame(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (DataAccessException e){
            System.out.println(e.getMessage());
        }
    }

    public String help(){
        if(Repl.state == State.SIGNEDOUT){
            return """
                    - Register <username, password, email>
                    - Login <username, password>
                    - Quit
                    """;
        }
        return """
                - Create Game <gameName>
                - Join Game <PlayerColor, gameID>
                - List Games
                - Logout
                - Clear All
                - Quit
                """;
    }
}
