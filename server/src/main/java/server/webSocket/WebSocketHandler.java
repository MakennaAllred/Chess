package server.webSocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import dataAccess.*;
import dataAccess.customExceptions.AlreadyTakenException;
import dataAccess.customExceptions.BadRequestException;
import dataAccess.customExceptions.DataAccessException;
import dataAccess.customExceptions.UnauthorizedException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.websocket.api.Session;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;


import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {

    private UserDataAccess users;
    private GameDataAccess games;
    private AuthDataAccess auths;
    private final ConnectionsManager connections = new ConnectionsManager();
    private HashMap<Integer, String> gamesAndUsers = new HashMap<>();

    public WebSocketHandler(GameDataAccess games, AuthDataAccess auths, UserDataAccess users){
        this.auths = auths;
        this.games = games;
        this.users = users;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) {
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
        try {
            JoinPlayer join = new Gson().fromJson(msg, JoinPlayer.class);
            String authToken = join.getAuthString();
            connections.add(authToken, session);
            AuthData auth = auths.getAuth(authToken);
            gamesAndUsers.put(join.gameID, auth.authToken());
            GameData gameInfo = games.getGame(join.gameID);
            String message = String.format("%s joined as %s player", auth.username(), join.playerColor);
            LoadGame notification = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameInfo);
            connections.clientNotify(auth.authToken(), notification);
            // server sends NOTIFICATION msg to other clients in game notifying what color root client is joining as
            Notification notif = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(auth.authToken(), notif);
        } catch (UnauthorizedException | IOException | BadRequestException | DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    private void joinObserver(String msg, Session session) {
        try {
            JoinObserver observerCom = new Gson().fromJson(msg, JoinObserver.class);
            connections.add(observerCom.getAuthString(), session);
            AuthData userInfo = auths.getAuth(observerCom.getAuthString());
            gamesAndUsers.put(observerCom.gameID, userInfo.authToken());
            GameData gameInfo = games.getGame(observerCom.gameID);
            String message = String.format("%s joined as observer", userInfo.username());
            LoadGame notification = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameInfo);
            connections.clientNotify(userInfo.authToken(), notification);
            Notification notif = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(userInfo.authToken(), notif);
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
                gamesAndUsers.remove(leaveCommand.gameID);

            } else if (Objects.equals(userInfo.username(), gameInfo.whiteUsername())) {
                games.removeUser(gameInfo, ChessGame.TeamColor.WHITE);
                message = String.format("%s stopped playing as the white user", userInfo.username());
                Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                connections.broadcast(userInfo.authToken(), notification);
                connections.remove(leaveCommand.getAuthString());
                gamesAndUsers.remove(leaveCommand.gameID);
            }
            message = String.format("%s stopped observing the game", userInfo.username());
            Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(userInfo.authToken(), notification);
            connections.remove(leaveCommand.getAuthString());
            gamesAndUsers.remove(leaveCommand.gameID);
        } catch (UnauthorizedException | IOException | BadRequestException | DataAccessException e) {
            System.out.println(e.getMessage());
        }

    }

    ;

    private void resign(String msg, Session session) {
        //server marks game as game over(no more moves can be made)
        try {
            Resign resignCommand = new Gson().fromJson(msg, Resign.class);
            AuthData userInfo = auths.getAuth(resignCommand.getAuthString());
            GameData gameInfo = games.getGame(resignCommand.gameID);
            gameInfo.game().setGameOver(true);
            String black = gameInfo.blackUsername();
            String white = gameInfo.whiteUsername();
            if (Objects.equals(userInfo.username(), white)) {
                games.removeUser(gameInfo, ChessGame.TeamColor.WHITE);
            }
            if (Objects.equals(userInfo.username(), black)) {
                games.removeUser(gameInfo, ChessGame.TeamColor.BLACK);

            }
            String message = String.format("%s resigned from the game. The game is over", userInfo.username());
            Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(userInfo.authToken(), notification);
            connections.remove(userInfo.authToken());
            gamesAndUsers.remove(gameInfo.gameID(), userInfo.authToken());
            //game is updated in db
        } catch (UnauthorizedException | IOException | BadRequestException | DataAccessException e) {
            System.out.println(e.getMessage());
        }

    }

    ;

    private void makeMove(String msg, Session session) {
        try {
            //verify valid move
            MakeMove moveCommand = new Gson().fromJson(msg,MakeMove.class);
            boolean isValid = false;
            AuthData userInfo = auths.getAuth(moveCommand.getAuthString());
            GameData gameInfo = games.getGame(moveCommand.gameID);
            ChessPosition start = moveCommand.move.getStartPosition();
            Collection<ChessMove> validMoves = gameInfo.game().validMoves(start);
            for (ChessMove move : validMoves) {
                if (move == moveCommand.move) {
                    isValid = true;
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
            }
        } catch (UnauthorizedException | IOException | BadRequestException | InvalidMoveException |
                 DataAccessException e) {
            System.out.println(e.getMessage());
        }

    }

    ;
}


