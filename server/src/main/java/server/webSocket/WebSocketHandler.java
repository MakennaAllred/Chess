package server.webSocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import dataAccess.*;
import jsonObjects.customExceptions.BadRequestException;
import jsonObjects.customExceptions.DataAccessException;
import jsonObjects.customExceptions.UnauthorizedException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;


import java.io.IOException;
import java.util.*;

@WebSocket
public class WebSocketHandler {

    private GameDataAccess games;
    private AuthDataAccess auths;
//    private final ConnectionsManager connections = new ConnectionsManager();
    private Map <Integer, ConnectionsManager> gameOrganizer = new HashMap<>();
    //map gameID and connection manager
    private Map<Integer, List<String>> gamesAndUsers = new HashMap<>() {
    };
    public Map<Integer, String> gameOver = new HashMap<>();

    public WebSocketHandler(GameDataAccess games, AuthDataAccess auths, UserDataAccess users){
        this.auths = auths;
        this.games = games;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException {
        System.out.println("received message");
        UserGameCommand userGameCommand = new Gson().fromJson(msg, UserGameCommand.class);
        switch (userGameCommand.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(msg, session);
            case JOIN_OBSERVER -> joinObserver(msg, session);
            case LEAVE -> leave(msg, session);
            case RESIGN -> resign(msg, session);
            case MAKE_MOVE -> makeMove(msg, session);
        }
    }
    public void clientsInGameCheck(String authToken, int gameID){
        List<String> clientsInGame = gamesAndUsers.get(gameID);
        if(clientsInGame != null) {
            clientsInGame.add((authToken));
        }
        else{
            clientsInGame = new ArrayList<>();
            clientsInGame.add(authToken);
        }
    }
    private void joinPlayer(String msg, Session session) {
        try {
            JoinPlayer join = new Gson().fromJson(msg, JoinPlayer.class);
            String authToken = join.getAuthString();
            AuthData auth = auths.getAuth(authToken);
            if (gameOrganizer.containsKey(join.gameID)){
                ConnectionsManager con = gameOrganizer.get(join.gameID);
                con.add(join.getAuthString(),session);
            }else{
                ConnectionsManager con = new ConnectionsManager();
                con.add(join.getAuthString(),session);
                gameOrganizer.put(join.gameID, con);
            }
            if(auth != null) {
                GameData gameInfo = games.getGame(join.gameID);
                if (gameInfo != null) {
                    if(join.playerColor == ChessGame.TeamColor.WHITE) {
                        if (Objects.equals(auth.username(), gameInfo.whiteUsername())) {
                            String message = String.format("%s joined as %s player", auth.username(), join.playerColor);
                            LoadGame notification = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameInfo);
                            clientsInGameCheck(authToken,gameInfo.gameID());
                            List<String> clientsInGame = gamesAndUsers.get(gameInfo.gameID());
                            gamesAndUsers.put(gameInfo.gameID(), clientsInGame);
                            Notification notif = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                            ConnectionsManager c = gameOrganizer.get(join.gameID);
                            c.broadcast(auth.authToken(), notif);
                            c.clientNotify(auth.authToken(), notification);

                        }else{
                            Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "player hasn't joined game properly");
                            ConnectionsManager c = gameOrganizer.get(join.gameID);
                            c.clientNotify(auth.authToken(), notification);
                        }
                    }else {
                        if (Objects.equals(auth.username(), gameInfo.blackUsername())) {
                            String message = String.format("%s joined as %s player", auth.username(), join.playerColor);
                            LoadGame notification = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameInfo);
                            List<String> clientsInGame = gamesAndUsers.get(gameInfo.gameID());
                            clientsInGameCheck(authToken, gameInfo.gameID());
                            gamesAndUsers.put(gameInfo.gameID(), clientsInGame);
                            ConnectionsManager c = gameOrganizer.get(join.gameID);
                            c.clientNotify(auth.authToken(), notification);
                            Notification notif = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                            c.broadcast(auth.authToken(), notif);
                        } else {
                            ConnectionsManager c = gameOrganizer.get(join.gameID);
                            Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "player hasn't joined game properly");
                            c.clientNotify(auth.authToken(), notification);
                        }
                    }
                } else {
                    ConnectionsManager c = gameOrganizer.get(join.gameID);
                    Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Bad gameID");
                    c.clientNotify(auth.authToken(), notification);
                }
            }else{
                ConnectionsManager c = gameOrganizer.get(join.gameID);
                Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Bad authtoken");
                c.clientNotify(authToken,notification);
            }
        } catch (UnauthorizedException | IOException | BadRequestException | DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    private void joinObserver(String msg, Session session) {
        try {
            JoinObserver observerCom = new Gson().fromJson(msg, JoinObserver.class);
            AuthData userInfo = auths.getAuth(observerCom.getAuthString());
            if (gameOrganizer.containsKey(observerCom.gameID)){
                ConnectionsManager con = gameOrganizer.get(observerCom.gameID);
                con.add(observerCom.getAuthString(),session);
            }else{
                ConnectionsManager con = new ConnectionsManager();
                con.add(observerCom.getAuthString(),session);
                gameOrganizer.put(observerCom.gameID, con);
            }
            if(userInfo != null) {
                GameData gameInfo = games.getGame(observerCom.gameID);
                if(gameInfo != null) {
                    String message = String.format("%s joined as observer", userInfo.username());
                    LoadGame notification = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameInfo);
                    ConnectionsManager c = gameOrganizer.get(observerCom.gameID);
                    c.clientNotify(userInfo.authToken(), notification);
                    Notification notif = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                    List<String> clientsInGame = gamesAndUsers.get(gameInfo.gameID());
                    if(clientsInGame != null) {
                        clientsInGame.add((userInfo.authToken()));
                    }
                    else{
                        clientsInGame = new ArrayList<>();
                        clientsInGame.add(userInfo.authToken());
                    }
                    gamesAndUsers.put(gameInfo.gameID(), clientsInGame);
                    c.broadcast(userInfo.authToken(), notif);
                }else{
                    ConnectionsManager c = gameOrganizer.get(observerCom.gameID);
                    Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Bad gameID");
                    c.clientNotify(userInfo.authToken(), notification);
                }
            }else{
                ConnectionsManager c = gameOrganizer.get(observerCom.gameID);
                Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Bad authtoken");
                c.clientNotify(observerCom.getAuthString(), notification);
            }
        } catch (UnauthorizedException | IOException | BadRequestException | DataAccessException e) {
            System.out.println(e.getMessage());
        }

    }

    ;

    private void leave(String msg, Session session) {
        try {
            Leave leaveCommand = new Gson().fromJson(msg, Leave.class);
            String message;
            GameData gameInfo = games.getGame(leaveCommand.gameID);
            AuthData userInfo = auths.getAuth(leaveCommand.getAuthString());
            if (Objects.equals(userInfo.username(), gameInfo.blackUsername())) {
                games.removeUser(gameInfo, ChessGame.TeamColor.BLACK);
                message = String.format("%s stopped playing as the black user", userInfo.username());
                Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                ConnectionsManager c = gameOrganizer.get(gameInfo.gameID());
                c.broadcast(userInfo.authToken(), notification);
                c.remove(leaveCommand.getAuthString());
                List<String> clientsInGame = gamesAndUsers.get(gameInfo.gameID());
                clientsInGame.remove((leaveCommand.getAuthString()));
                gamesAndUsers.put(gameInfo.gameID(), clientsInGame);

            } else if (Objects.equals(userInfo.username(), gameInfo.whiteUsername())) {
                games.removeUser(gameInfo, ChessGame.TeamColor.WHITE);
                message = String.format("%s stopped playing as the white user", userInfo.username());
                Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                ConnectionsManager c = gameOrganizer.get(gameInfo.gameID());
                c.broadcast(userInfo.authToken(), notification);
                c.remove(leaveCommand.getAuthString());
                List<String> clientsInGame = gamesAndUsers.get(gameInfo.gameID());
                clientsInGame.remove((leaveCommand.getAuthString()));
                gamesAndUsers.put(gameInfo.gameID(), clientsInGame);
            }
            else {
                message = String.format("%s stopped observing the game", userInfo.username());
                Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                ConnectionsManager c = gameOrganizer.get(gameInfo.gameID());
                c.broadcast(userInfo.authToken(), notification);
                c.remove(leaveCommand.getAuthString());
                List<String> clientsInGame = gamesAndUsers.get(gameInfo.gameID());
                clientsInGame.remove((leaveCommand.getAuthString()));
                gamesAndUsers.put(gameInfo.gameID(), clientsInGame);
            }
        } catch (UnauthorizedException | IOException | BadRequestException | DataAccessException e) {
            System.out.println(e.getMessage());
        }

    }


    private void resign(String msg, Session session) {
        try {
            Resign resignCommand = new Gson().fromJson(msg, Resign.class);
            AuthData userInfo = auths.getAuth(resignCommand.getAuthString());
            GameData gameInfo = games.getGame(resignCommand.gameID);
            if(gameInfo.game().isGameOver){
                Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Can't resign, game is over");
                ConnectionsManager c = gameOrganizer.get(gameInfo.gameID());
                c.clientNotify(userInfo.authToken(), notification);
                return;
            }
            List<String> clientsInGame = gamesAndUsers.get(gameInfo.gameID());
            Iterator<String> iterator = clientsInGame.iterator();
            while (iterator.hasNext()) {
                String auth = iterator.next();
                if (Objects.equals(auth, userInfo.authToken())) {
                    gameInfo.game().setGameOver(true);
                    games.updateGame(gameInfo.game(),gameInfo.gameID());
                    String black = gameInfo.blackUsername();
                    String white = gameInfo.whiteUsername();
                    if(!Objects.equals(userInfo.username(), black) && !Objects.equals(userInfo.username(), white)){
                        Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Can't resign as observer");
                        ConnectionsManager c = gameOrganizer.get(gameInfo.gameID());
                        c.clientNotify(userInfo.authToken(), notification);
                        break;
                    }
                    iterator.remove();
                    String message = String.format("%s resigned from the game. The game is over", userInfo.username());
                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                    ConnectionsManager c = gameOrganizer.get(gameInfo.gameID());
                    c.broadcast(userInfo.authToken(), notification);
                    c = gameOrganizer.get(gameInfo.gameID());
                    c.clientNotify(userInfo.authToken(), notification);
                    c.remove(userInfo.authToken());
                    gamesAndUsers.put(gameInfo.gameID(), clientsInGame);
                }
            }
        } catch (UnauthorizedException | IOException | BadRequestException | DataAccessException e) {
            System.out.println(e.getMessage());
        }

    }



    private void makeMove(String msg, Session session) throws IOException {
        try {
            MakeMove moveCommand = new Gson().fromJson(msg, MakeMove.class);
            boolean isValid = false;
            AuthData userInfo = auths.getAuth(moveCommand.getAuthString());
            GameData gameInfo = games.getGame(moveCommand.gameID);
            if(gameInfo.game().isGameOver){
                Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Can't make move, game is over");
                ConnectionsManager c = gameOrganizer.get(gameInfo.gameID());
                c.clientNotify(userInfo.authToken(), notification);
                return;
            }
            String black = gameInfo.blackUsername();
            String white = gameInfo.whiteUsername();
            if (Objects.equals(userInfo.username(), black)) {
                if (gameInfo.game().getTeamTurn() != ChessGame.TeamColor.BLACK) {
                    Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Not your turn, cannot make move");
                    ConnectionsManager c = gameOrganizer.get(gameInfo.gameID());
                    c.clientNotify(moveCommand.getAuthString(), notification);
                }else{
                    ChessPosition start = moveCommand.move.getStartPosition();
                    Collection<ChessMove> validMoves = gameInfo.game().validMoves(start);
                    for (ChessMove move : validMoves) {
                        if (move.equals(moveCommand.move)) {
                            isValid = true;
                            break;
                        }
                    }
                    if (isValid) {
                        gameInfo.game().makeMove(moveCommand.move);
                        games.updateGame(gameInfo.game(), gameInfo.gameID());
                        String message = String.format("%s made a move", userInfo.username());
                        Notification noti = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                        LoadGame notification = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameInfo);
                        ConnectionsManager c = gameOrganizer.get(gameInfo.gameID());
                        c.broadcast("", notification);
                        c.broadcast(userInfo.authToken(), noti);
                    } else {
                        Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Not a valid move");
                        ConnectionsManager c = gameOrganizer.get(gameInfo.gameID());
                        c.clientNotify(moveCommand.getAuthString(), notification);
                    }
                }
            }
            else if(Objects.equals(userInfo.username(), white)){
                if(gameInfo.game().getTeamTurn() != ChessGame.TeamColor.WHITE){
                    Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Not your turn, cannot make move");
                    ConnectionsManager c = gameOrganizer.get(gameInfo.gameID());
                    c.clientNotify(moveCommand.getAuthString(), notification);
                }else{
                    ChessPosition start = moveCommand.move.getStartPosition();
                    Collection<ChessMove> validMoves = gameInfo.game().validMoves(start);
                    for (ChessMove move : validMoves) {
                        if (move.equals(moveCommand.move)) {
                            isValid = true;
                            break;
                        }
                    }
                    if (isValid) {
                        gameInfo.game().makeMove(moveCommand.move);
                        games.updateGame(gameInfo.game(), gameInfo.gameID());
                        String message = String.format("%s made a move", userInfo.username());
                        Notification noti = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                        LoadGame notification = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameInfo);
                        ConnectionsManager c = gameOrganizer.get(gameInfo.gameID());
                        c.broadcast(userInfo.authToken(), notification);
                        c.clientNotify(userInfo.authToken(), notification);
                        c.broadcast(userInfo.authToken(), noti);
                    } else {
                        ConnectionsManager c = gameOrganizer.get(gameInfo.gameID());
                        Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Not a valid move");
                        c.clientNotify(moveCommand.getAuthString(), notification);
                    }
                }

            }else{
                Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Cannot make moves as an observer");
                ConnectionsManager c = gameOrganizer.get(gameInfo.gameID());
                c.clientNotify(moveCommand.getAuthString(), notification);
            }
        }

        catch (UnauthorizedException | IOException | BadRequestException | InvalidMoveException |
                 DataAccessException e) {
            System.out.println(e.getMessage());
        }

    }

}




