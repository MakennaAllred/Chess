package ui;

import chess.ChessGame;
import jsonObjects.CreateGameRes;
import jsonObjects.JoinGameReq;
import jsonObjects.ListGamesRes;
import model.GameData;
import ui.webSocket.WebSocketFacade;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PostLoginMenu {
    static Map<Integer, GameData> listOfGames = new HashMap<>();
public static String port;
    public static Object eval(String port, String input, WebSocketFacade socket){
        PostLoginMenu.port = port;
        try{
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens,1,tokens.length);
            return switch(cmd){
                case "join" -> joinGame(socket, input, params);
                case "create" -> createGame(params);
                case "list" -> listGames(params);
                case "logout" -> logout(params);
                case "clear" -> clearAll(params);
                case "quit" -> "quit";
                default -> Repl.help();
            };
        } catch (Exception e){
            return e.getMessage();
        }
    }


    public static String joinGame(WebSocketFacade socket, String line, String... params){
        try {
            if (params.length >= 1) {
                String playerColor = params[0].toUpperCase();
                int gameID = Integer.parseInt(params[1]);
                Repl.gameID = gameID;
                JoinGameReq body = new JoinGameReq(playerColor,gameID);
                try {
                    new ServerFacade(PostLoginMenu.port).joinGame(Repl.auth.authToken(), body);
                    Repl.game = listOfGames.get(gameID);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                if(playerColor == null){
                    InGame.state = InGameStates.OBSERVER;
                    socket.joinObserverWS(Repl.auth.authToken(),gameID);

                }
                else {
                    InGame.state = InGameStates.PLAYER;
                    if (playerColor.equals("WHITE")) {
                        socket.joinPlayerWs(Repl.auth.authToken(), gameID, ChessGame.TeamColor.WHITE);

                    }
                    else{
                        socket.joinPlayerWs(Repl.auth.authToken(), gameID, ChessGame.TeamColor.BLACK);

                    }
                }
//                GenerateBoard.generateBoard(ChessGame.TeamColor.BLACK, Repl.game.game().getBoard(), null, null);
                System.out.println();
//                GenerateBoard.generateBoard(ChessGame.TeamColor.WHITE, Repl.game.game().getBoard(), null, null);
                Repl.state = State.INGAME;
                System.out.println("Joined game");
                Repl.help();

            }
            else{
                System.out.print("Error: can't join game");
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
                GameData gameBody = new GameData(0,null,null,gameName,new ChessGame());
                try{
                    CreateGameRes game = new ServerFacade(PostLoginMenu.port).createGame(Repl.auth.authToken(),gameBody);
                    System.out.print("Game " + game.gameID() + " created");
                    listOfGames.put(game.gameID(),gameBody);
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
        listOfGames = new HashMap<>();
        try{
            ListGamesRes games = new ServerFacade(PostLoginMenu.port).listGames(Repl.auth.authToken());
            //for each games.games(), don't print the board
            for(GameData game :games.games()) {
                System.out.print("gameID: " + game.gameID() + " ");
                System.out.print("White Username: " + game.whiteUsername() + " ");
                System.out.print("Black Username: " + game.blackUsername() + " ");
                System.out.print("Game name: " + game.gameName());
                System.out.print("\n");
                listOfGames.put(game.gameID(), game);
            }
            return games;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    public static Void logout(String...params){
        try{
            new ServerFacade(PostLoginMenu.port).logout(Repl.auth.authToken());
            Repl.state = State.SIGNEDOUT;
            System.out.println("Logged out successfully");
            Repl.help();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public static Void clearAll(String...params){
        try{
            new ServerFacade(PostLoginMenu.port).deleteAll(Repl.auth.authToken());
            Repl.state = State.SIGNEDOUT;
            System.out.println("Everything cleared");
            Repl.help();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
