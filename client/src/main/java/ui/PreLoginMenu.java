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

    public static String login(String... params) {
        try {
            if (params.length >= 1) {
                String username = params[0];
                String password = params[1];
                UserData user = new UserData(username, password, null);
                try {
                    AuthData auth = new ServerFacade().login(user);
                    Repl.state = State.SIGNEDIN;
                    return auth.authToken();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Error";
    }

    public static String register(String... params) {
        try {
            if (params.length >= 1) {
                String username = params[0];
                String password = params[1];
                String email = params[2];
                UserData user = new UserData(username, password, email);
                try {
                    AuthData auth = new ServerFacade().register(user);
                    Repl.state = State.SIGNEDIN;
                    return auth.authToken();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

        } catch (Exception e) {
            return e.getMessage();
        }
        return "You have now registered";
    }
}

