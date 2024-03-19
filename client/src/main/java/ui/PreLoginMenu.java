package ui;

import dataAccess.customExceptions.DataAccessException;
import model.UserData;

import java.util.Arrays;

public class PreLoginMenu {

    public String eval(String input){
        try{
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens,1,tokens.length);
            return switch(cmd){
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (DataAccessException e){
            return e.getMessage();
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

    public String login(String...params) {
        try{
            if(params.length >= 1){
                String username = params[1];
                String password = params[2];
                UserData user = new UserData(username,password,null);
                try {
                    new ServerFacade().login(user);
                    Repl.state = State.SIGNEDIN;
                }catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}

