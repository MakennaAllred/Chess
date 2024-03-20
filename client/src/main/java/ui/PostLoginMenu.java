package ui;

import dataAccess.JoinGameReq;
import dataAccess.customExceptions.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.Arrays;

public class PostLoginMenu {
    private static AuthData auth;

    public static String eval(String input){
        try{
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens,1,tokens.length);
            return switch(cmd){
                case "join game" -> joinGame(params);
                case "create game" -> createGame(params);
                case "list games" -> listGames(params);
                case "logout" -> logout(params);
                case "clear all" -> clearAll(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception e){
            return e.getMessage();
        }
    }

    public static String help(){
        if(Repl.state == State.SIGNEDOUT){
            return """
                    - Register <username password email>
                    - Login <username password>
                    - Quit
                    """;
        }
        return """
                - Create Game <gameName>
                - Join Game <PlayerColor gameID>
                - List Games
                - Logout
                - Clear All
                - Quit
                """;
    }
    public static String joinGame(String...params){
        try {
            if (params.length >= 1) {
                String playerColor = params[0];
                int gameID = Integer.parseInt(params[1]);
                JoinGameReq body = new JoinGameReq(playerColor,gameID);
                try {
                    new ServerFacade().joinGame(auth.authToken(), body);
                    Repl.state = State.SIGNEDIN;
                    return auth.authToken();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return null;
    }


    public static String createGame(String...params){
        try{
            if(params.length>=1){
                String gameName = params[0];

            }
        }
        return "null";
    }
    public static String listGames(String...params){
        return null;
    }
    public static String logout(String...params){
        return "null";
    }
    public static String clearAll(String...params){
        return null;
    }
}
