package ui;

import dataAccess.CreateGameRes;
import dataAccess.JoinGameReq;
import dataAccess.ListGamesRes;
import dataAccess.customExceptions.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Arrays;

public class PostLoginMenu {

    public static Object eval(String input){
        try{
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens,1,tokens.length);
            return switch(cmd){
                case "join" -> joinGame(params);
                case "create" -> createGame(params);
                case "list" -> listGames(params);
                case "logout" -> logout(params);
                case "clear" -> clearAll(params);
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
                - Create <gameName>
                - Join <PlayerColor gameID>
                - List
                - Logout
                - Clear
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
                    new ServerFacade().joinGame(Repl.auth.authToken(), body);
                    Repl.state = State.SIGNEDIN;
                    return Repl.auth.authToken();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return null;
    }


    public static int createGame(String...params){
        try{
            if(params.length>=1){
                String gameName = params[0];
                GameData gameBody = new GameData(0,null,null,gameName,null);
                try{
                    CreateGameRes game = new ServerFacade().createGame(Repl.auth.authToken(),gameBody);
                    System.out.print("Game" + game.gameID() + "created");
                    return game.gameID();
                }catch (Exception e){
                    System.out.print(e.getMessage());
                }
            }
        }
        catch(Exception e){
            System.out.print(e.getMessage());
        }
        return 0;
    }

    public static ListGamesRes listGames(String...params){
        try{
            ListGamesRes games = new ServerFacade().listGames(Repl.auth.authToken());
            System.out.println(games);
            return games;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    public static Void logout(String...params){
        try{
            new ServerFacade().logout(Repl.auth.authToken());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println("Logged out successfully");
        return null;
    }
    public static Void clearAll(String...params){
        try{
            new ServerFacade().deleteAll(Repl.auth.authToken());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println("All games cleared");
        return null;
    }
}
