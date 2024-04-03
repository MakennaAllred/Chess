package server.webSocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import dataAccess.SQLAuthDao;
import dataAccess.SQLGameDao;
import dataAccess.SQLUserDao;
import dataAccess.customExceptions.BadRequestException;
import dataAccess.customExceptions.DataAccessException;
import dataAccess.customExceptions.UnauthorizedException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.MakeMove;
import webSocketMessages.userCommands.UserGameCommand;


import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

public class WebSocketHandler {
    private SQLAuthDao auths;
    private SQLGameDao games;
    private final ConnectionsManager connections = new ConnectionsManager();
    private HashMap<Integer,String> gamesAndUsers = new HashMap<>();

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException, UnauthorizedException {
        UserGameCommand userGameCommand = new Gson().fromJson(msg,UserGameCommand.class);
        switch(userGameCommand.getCommandType()){
            case JOIN_PLAYER -> joinPlayer(userGameCommand,session);
            case JOIN_OBSERVER -> joinObserver(userGameCommand,session);
            case LEAVE -> leave(userGameCommand,session);
            case RESIGN -> resign(userGameCommand,session);
            case MAKE_MOVE -> makeMove(userGameCommand, session);
        }
    }

    private void joinPlayer(UserGameCommand command, Session session) throws UnauthorizedException, IOException {
        connections.add(command.getAuthString(),session);
        JoinPlayer join = new Gson().fromJson(command,JoinPlayer.class);
        AuthData auth = auths.getAuth(command.getAuthString());
        gamesAndUsers.put(join.gameID,auth.authToken());
        String message = String.format("%s joined as %s player",auth.username(),join.playerColor);
        //**FIXME:server sends LOAD GAME msg back to root client
        // server sends NOTIFICATION msg to other clients in game notifying what color root client is joining as
        Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION,message);
        connections.broadcast(auth.authToken(),notification);
    }
    private void joinObserver(UserGameCommand command, Session session) throws UnauthorizedException, IOException {
        connections.add(command.getAuthString(),session);
        JoinObserver observerCom = new Gson().fromJson(command, JoinObserver.class);
        AuthData userInfo = auths.getAuth(command.getAuthString());
        gamesAndUsers.put(observerCom.gameID,userInfo.authToken());
        String message = String.format("%s joined as observer",userInfo.username());
        //FIXME:sends load game message back to root client
        Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION,message);
        connections.broadcast(userInfo.authToken(),notification);
        //sends NOTIFICATION msg to other clients in that game informing root joined as observer
    };
    private void leave(UserGameCommand com, Session session) throws UnauthorizedException, IOException {
        //game is updated to remove root client, game is updated in db
        AuthData userInfo = auths.getAuth(com.getAuthString());
        connections.remove(com.getAuthString());
        String message = String.format("%s stopped observing the game",userInfo.username());
        Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION,message);
        connections.broadcast(userInfo.authToken(), notification);
        //server sends NOTIFICATION message to other clients informing them root left
    };
    private void resign(UserGameCommand command, Session session){
        //server marks game as game over(no more moves can be made)
        //game is updated in db
        //server sends NOTIFICATION message to all clients that root left
    };
    private void makeMove(UserGameCommand command, Session session) throws BadRequestException, DataAccessException, UnauthorizedException, IOException {
        //verify valid move
        boolean isValid = false;
        AuthData userInfo = auths.getAuth(command.getAuthString());
        MakeMove moveCommand = new Gson().fromJson(command, MakeMove.class);
        GameData gameInfo = games.getGame(moveCommand.gameID);
        ChessPosition start = moveCommand.move.getStartPosition();
        Collection<ChessMove> validMoves = gameInfo.game().validMoves(start);
        for(ChessMove move : validMoves){
            if(move == moveCommand.move){
                isValid = true;
            }
        }
        if(isValid){
            //FIXME: game is updated to represent move and game is updated in db
            String message = String.format("%s made a move", userInfo.username());
            Notification noti = new Notification(ServerMessage.ServerMessageType.NOTIFICATION,message);
            LoadGame notification = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME,gameInfo);
            connections.broadcast("", notification);
            connections.broadcast(userInfo.authToken(), noti);
        }

    };
}
