package ui;

import dataAccess.customExceptions.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.Arrays;

public class PreLoginMenu {


    public static String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static String help() {
        if (Repl.state == State.SIGNEDOUT) {
            return """
                    - Register <username password email>
                    - Login <username password>
                    - Quit
                    """;
        }
        return """
                - Create <gameName>
                - Join <PlayerColor gameID>
                - List
                - Logout
                - Clear
                - Quit
                """;
    }

    public static String login(String... params) {
        try {
            if (params.length >= 1) {
                String username = params[0];
                String password = params[1];
                if (username != null && password != null) {
                    UserData user = new UserData(username, password, null);
                    try {
                        Repl.auth = new ServerFacade().login(user);
                        Repl.username = Repl.auth.username();
                        Repl.state = State.SIGNEDIN;
                        System.out.print(help());
                        return Repl.auth.authToken();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            else {
                System.out.println("Enter username and password to log in");
            }
            } catch(Exception e){
                return e.getMessage();
            }
        return "Error: logging in";
    }

    public static String register(String... params) {
        try {
            if (params.length >= 1) {
                String username = params[0];
                String password = params[1];
                String email = params[2];
                if (username != null && password != null && email != null) {
                    UserData user = new UserData(username, password, email);
                    try {
                        Repl.auth = new ServerFacade().register(user);
                        Repl.username = Repl.auth.username();
                        Repl.state = State.SIGNEDIN;
                        System.out.print(help());
                        return Repl.auth.authToken();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            else{
                System.out.println("Please give a username, password, and email to register");
            }

        } catch (Exception e) {
            return e.getMessage();
        }
        return "You have now registered";
    }
}

