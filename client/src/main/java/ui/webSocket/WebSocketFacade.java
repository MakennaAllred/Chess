package ui.webSocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    public Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler){
        try{
            url = url.replace("http", "ws");
            URI socketURI = new URI(url+"/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this,socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>(){
                @Override
                public void onMessage(String message){
                    ServerMessage notification = new Gson().fromJson(message,ServerMessage.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (URISyntaxException | DeploymentException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onOpen(javax.websocket.Session session, EndpointConfig endpointConfig) {

    }
    public void send(String s){
        try{
            this.session.getBasicRemote().sendText((s));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void joinPlayerWs(String authToken,int gameID, ChessGame.TeamColor playerColor){
        JoinPlayer v = new JoinPlayer(authToken, UserGameCommand.CommandType.JOIN_PLAYER, gameID, playerColor);
        String join = new Gson().toJson(v);
        send(join);
    }
    public void joinObserverWS(String authToken, int gameID){
        JoinObserver o = new JoinObserver(authToken,gameID);
        String obs = new Gson().toJson(o);
        send(obs);
    }
    public void makeMove(String authToken, int gameID, ChessMove move){
        MakeMove mv = new MakeMove(authToken, UserGameCommand.CommandType.MAKE_MOVE,gameID,move);
        String m = new Gson().toJson(mv);
        send(m);
    }
    public void leave(String authToken, int gameID){
        Leave l = new Leave(authToken, UserGameCommand.CommandType.LEAVE,gameID);
        String lv = new Gson().toJson(l);
        send(lv);
    }
    public void resign(String authToken, int gameID){
        Resign r = new Resign(authToken, UserGameCommand.CommandType.RESIGN,gameID);
        String rs = new Gson().toJson(r);
        send(rs);
    }

}