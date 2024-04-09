package server.webSocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import dataAccess.*;
import dataAccess.customExceptions.BadRequestException;
import dataAccess.customExceptions.DataAccessException;
import dataAccess.customExceptions.UnauthorizedException;
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

    private UserDataAccess users;
    private GameDataAccess games;
    private AuthDataAccess auths;
    private final ConnectionsManager connections = new ConnectionsManager();
    private Map<Integer, List<String>> gamesAndUsers = new HashMap<>() {
    };

    public WebSocketHandler(GameDataAccess games, AuthDataAccess auths, UserDataAccess users){
        this.auths = auths;
        this.games = games;
        this.users = users;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) {
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

    private void joinPlayer(String msg, Session session) {
        //values.removeif
        try {
            JoinPlayer join = new Gson().fromJson(msg, JoinPlayer.class);
            String authToken = join.getAuthString();
            connections.add(authToken, session);
            AuthData auth = auths.getAuth(authToken);
            if(auth != null) {
                GameData gameInfo = games.getGame(join.gameID);
                if (gameInfo != null) {
                    if(join.playerColor == ChessGame.TeamColor.WHITE) {
                        if (Objects.equals(auth.username(), gameInfo.whiteUsername())) {
                            String message = String.format("%s joined as %s player", auth.username(), join.playerColor);
                            LoadGame notification = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameInfo);
                            List<String> clientsInGame = gamesAndUsers.get(gameInfo.gameID());
                            if(clientsInGame != null) {
                                clientsInGame.add((authToken));
                            }
                            else{
                                clientsInGame = new ArrayList<>();
                                clientsInGame.add(authToken);
                                }
                            gamesAndUsers.put(gameInfo.gameID(), clientsInGame);
                            connections.clientNotify(auth.authToken(), notification);
                            Notification notif = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                            connections.broadcast(auth.authToken(), notif);

                        }else{
                            Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "player hasn't joined game properly");
                            connections.clientNotify(auth.authToken(), notification);
                        }
                    }else {
                        if (Objects.equals(auth.username(), gameInfo.blackUsername())) {
                            String message = String.format("%s joined as %s player", auth.username(), join.playerColor);
                            LoadGame notification = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameInfo);
                            List<String> clientsInGame = gamesAndUsers.get(gameInfo.gameID());
                            if(clientsInGame != null) {
                                clientsInGame.add((authToken));
                            }
                            else{
                                clientsInGame = new ArrayList<>();
                                clientsInGame.add(authToken);
                            }
                            gamesAndUsers.put(gameInfo.gameID(), clientsInGame);
                            connections.clientNotify(auth.authToken(), notification);
                            Notification notif = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                            connections.broadcast(auth.authToken(), notif);
                        } else {
                            Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "player hasn't joined game properly");
                            connections.clientNotify(auth.authToken(), notification);
                        }
                    }
                } else {
                    Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Bad gameID");
                    connections.clientNotify(auth.authToken(), notification);
                }
            }else{
                Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Bad authtoken");
                connections.clientNotify(authToken,notification);
            }
        } catch (UnauthorizedException | IOException | BadRequestException | DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    private void joinObserver(String msg, Session session) {
        try {
            JoinObserver observerCom = new Gson().fromJson(msg, JoinObserver.class);
            connections.add(observerCom.getAuthString(), session);
            AuthData userInfo = auths.getAuth(observerCom.getAuthString());
            if(userInfo != null) {
                GameData gameInfo = games.getGame(observerCom.gameID);
                if(gameInfo != null) {
                    String message = String.format("%s joined as observer", userInfo.username());
                    LoadGame notification = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameInfo);
                    connections.clientNotify(userInfo.authToken(), notification);
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
                    connections.broadcast(userInfo.authToken(), notif);
                }else{
                    Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Bad gameID");
                    connections.clientNotify(userInfo.authToken(), notification);
                }
            }else{
                Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Bad authtoken");
                connections.clientNotify(observerCom.getAuthString(), notification);
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
                connections.broadcast(userInfo.authToken(), notification);
                connections.remove(leaveCommand.getAuthString());
                List<String> clientsInGame = gamesAndUsers.get(gameInfo.gameID());
                clientsInGame.remove((leaveCommand.getAuthString()));
                gamesAndUsers.put(gameInfo.gameID(), clientsInGame);



            } else if (Objects.equals(userInfo.username(), gameInfo.whiteUsername())) {
                games.removeUser(gameInfo, ChessGame.TeamColor.WHITE);
                message = String.format("%s stopped playing as the white user", userInfo.username());
                Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                connections.broadcast(userInfo.authToken(), notification);
                connections.remove(leaveCommand.getAuthString());
                List<String> clientsInGame = gamesAndUsers.get(gameInfo.gameID());
                clientsInGame.remove((leaveCommand.getAuthString()));
                gamesAndUsers.put(gameInfo.gameID(), clientsInGame);
            }
            else {
                message = String.format("%s stopped observing the game", userInfo.username());
                Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                connections.broadcast(userInfo.authToken(), notification);
                connections.remove(leaveCommand.getAuthString());
                List<String> clientsInGame = gamesAndUsers.get(gameInfo.gameID());
                clientsInGame.remove((leaveCommand.getAuthString()));
                gamesAndUsers.put(gameInfo.gameID(), clientsInGame);
            }
        } catch (UnauthorizedException | IOException | BadRequestException | DataAccessException e) {
            System.out.println(e.getMessage());
        }

    }


    private void resign(String msg, Session session) {
        String member = "";
        //server marks game as game over(no more moves can be made)
        try {
            Resign resignCommand = new Gson().fromJson(msg, Resign.class);
            AuthData userInfo = auths.getAuth(resignCommand.getAuthString());
            GameData gameInfo = games.getGame(resignCommand.gameID);
            List<String> clientsInGame = gamesAndUsers.get(gameInfo.gameID());
            for(String auth : clientsInGame) {
                if (Objects.equals(auth, userInfo.authToken())) {
                    gameInfo.game().setGameOver(true);
                    String black = gameInfo.blackUsername();
                    String white = gameInfo.whiteUsername();
                    if (Objects.equals(userInfo.username(), white)) {
                        games.removeUser(gameInfo, ChessGame.TeamColor.WHITE);

                    }
                    if (Objects.equals(userInfo.username(), black)) {
                        games.removeUser(gameInfo, ChessGame.TeamColor.BLACK);

                    }
                    member = auth;
                    gameInfo = games.getGame(resignCommand.gameID);
                    String message = String.format("%s resigned from the game. The game is over", userInfo.username());
                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                    connections.broadcast(userInfo.authToken(), notification);
                    connections.remove(userInfo.authToken());
                    connections.clientNotify(userInfo.authToken(),notification);
//                    clientsInGame.removeIf(auth -> Objects.equals(auth, userInfo.authToken()));
//                    gamesAndUsers.put(gameInfo.gameID(), clientsInGame);
                } else {
                    Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Bad authtoken");
                    connections.clientNotify(resignCommand.getAuthString(), notification);
                }
            }
            if(member != null) {
                clientsInGame.remove(member);
                gamesAndUsers.put(gameInfo.gameID(), clientsInGame);
            }
            //game is updated in db
        } catch (UnauthorizedException | IOException | BadRequestException | DataAccessException e) {
            System.out.println(e.getMessage());
        }

    }



    private void makeMove(String msg, Session session) {
        try {
            //verify valid move
            MakeMove moveCommand = new Gson().fromJson(msg, MakeMove.class);
            boolean isValid = false;
            AuthData userInfo = auths.getAuth(moveCommand.getAuthString());
            GameData gameInfo = games.getGame(moveCommand.gameID);
            String black = gameInfo.blackUsername();
            String white = gameInfo.whiteUsername();
            if (Objects.equals(userInfo.username(), black)) {
                if (gameInfo.game().getTeamTurn() != ChessGame.TeamColor.BLACK) {
                    Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Not your turn, cannot make move");
                    connections.clientNotify(moveCommand.getAuthString(), notification);
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
                        connections.broadcast("", notification);
                        connections.broadcast(userInfo.authToken(), noti);
                    } else {
                        Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Not a valid move");
                        connections.clientNotify(moveCommand.getAuthString(), notification);
                    }
                }
            }
            else if(Objects.equals(userInfo.username(), white)){
                if(gameInfo.game().getTeamTurn() != ChessGame.TeamColor.WHITE){
                    Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Not your turn, cannot make move");
                    connections.clientNotify(moveCommand.getAuthString(), notification);
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
                        connections.broadcast("", notification);
                        connections.broadcast(userInfo.authToken(), noti);
                    } else {
                        Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Not a valid move");
                        connections.clientNotify(moveCommand.getAuthString(), notification);
                    }
                }

            }else{
                Error notification = new Error(ServerMessage.ServerMessageType.ERROR, "Cannot make moves as an observer");
                connections.clientNotify(moveCommand.getAuthString(), notification);
            }
        }

        catch (UnauthorizedException | IOException | BadRequestException | InvalidMoveException |
                 DataAccessException e) {
            System.out.println(e.getMessage());
        }

    }

}



//Send resign initial confirmation message?
