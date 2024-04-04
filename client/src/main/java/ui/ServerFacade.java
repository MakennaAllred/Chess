package ui;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.CreateGameRes;
import dataAccess.JoinGameReq;
import dataAccess.ListGamesRes;
import dataAccess.customExceptions.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import ui.webSocket.NotificationHandler;
import ui.webSocket.WebSocketFacade;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;

public class ServerFacade {
    private final String serverURL;
    private NotificationHandler notificationHandler;
    private WebSocketFacade socket;

    public ServerFacade(int port, NotificationHandler notificationHandler){
        this.notificationHandler = notificationHandler;
        this.serverURL = "http://localhost:" + port;
        this.socket = new WebSocketFacade(serverURL, notificationHandler);
        //usergame commands and pass to websocket facade
    }
    public void joinPlayerWs(String authToken,int gameID, ChessGame.TeamColor playerColor){
        JoinPlayer v = new JoinPlayer(authToken, UserGameCommand.CommandType.JOIN_PLAYER, gameID, playerColor);
        String join = new Gson().toJson(v);
        socket.send(join);
    }
    public void joinObserverWS(String authToken, int gameID){
        JoinObserver o = new JoinObserver(authToken,gameID);
        String obs = new Gson().toJson(o);
        socket.send(obs);
    }
    public void makeMove(String authToken, int gameID, ChessMove move){
        MakeMove mv = new MakeMove(authToken, UserGameCommand.CommandType.MAKE_MOVE,gameID,move);
        String m = new Gson().toJson(mv);
        socket.send(m);
    }
    public void leave(String authToken, int gameID){
        Leave l = new Leave(authToken, UserGameCommand.CommandType.LEAVE,gameID);
        String lv = new Gson().toJson(l);
        socket.send(lv);
    }
    public void resign(String authToken, int gameID){
        Resign r = new Resign(authToken, UserGameCommand.CommandType.RESIGN,gameID);
        String rs = new Gson().toJson(r);
        socket.send(rs);
    }

    public AuthData login(UserData user) {
        String path = "/session";
        return this.makeRequest("POST",path, null, user, AuthData.class);
    }
    public AuthData register(UserData user){
        String path = "/user";
        return this.makeRequest("POST", path, null, user, AuthData.class);
    }
    public void logout(String authToken){
        String path = "/session";
        this.makeRequest("DELETE", path, authToken, null,null);
    }

    public ListGamesRes listGames(String authToken){
        String path = "/game";
        return this.makeRequest("GET", path, authToken, null, ListGamesRes.class);
    }


    public CreateGameRes createGame(String authToken, GameData gameBody){
        String path = "/game";
        return this.makeRequest("POST", path, authToken, gameBody, CreateGameRes.class);
    }

    public void joinGame(String authToken, JoinGameReq body){
        String path = "/game";
        this.makeRequest("PUT", path, authToken, body, null);
    }
    public void deleteAll(String auth){
        String path = "/db";
        this.makeRequest("DELETE", path, auth,null, null);
    }

    private <T> T makeRequest(String method, String path, String authToken, Object request, Class<T> responseClass){
        try{
            URL url = (new URI(serverURL + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            http.addRequestProperty("authorization", authToken);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http,responseClass);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException{
        if(request != null){
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try(OutputStream reqBody = http.getOutputStream()){
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException{
        var status = http.getResponseCode();
        if(!isSuccessful(status)){
            throw new RuntimeException("error:" + status);
        }
    }
    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status  == 200;
    }

}

//client
//websocket facade
//notification handler:takes in server messages

//server
//hashmaps to send messages
//websocket handler
//handler takes message: user game commands

//look at petshop, try to get it connected so you can send stuff across then look at methods