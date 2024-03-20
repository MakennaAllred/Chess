package ui;

import com.google.gson.Gson;
import dataAccess.CreateGameRes;
import dataAccess.JoinGameReq;
import dataAccess.ListGamesRes;
import dataAccess.customExceptions.DataAccessException;
import model.AuthData;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;

public class ServerFacade {
    private final String serverURL;

    public ServerFacade(){
        this.serverURL = "http://localhost:8080";}
    public AuthData login(UserData user) {
        String path = "/session";
        return this.makeRequest("POST",path, user, AuthData.class);
    }
    public AuthData register(UserData user){
        String path = "/user";
        return this.makeRequest("POST", path, user, AuthData.class);
    }
    public void logout(AuthData authToken){
        String path = "/session";
        this.makeRequest("DELETE", path, authToken, null);
    }

    public ListGamesRes listGames(AuthData authToken){
        String path = "/game";
        return this.makeRequest("GET", path, authToken, ListGamesRes.class);
    }


    public CreateGameRes createGame(AuthData authToken){
        String path = "/game";
        return this.makeRequest("POST", path, authToken, CreateGameRes.class);
    }

    public void joinGame(String authToken, JoinGameReq body){
        String path = "/game";
        this.makeRequest("PUT", path, JoinGameReq.class, null);
    }
    public void deleteAll(AuthData auth){
        String path = "/db";
        this.makeRequest("DELETE", path, auth, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass){
        try{
            URL url = (new URI(serverURL + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

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

    public void throwIfNotSuccessful(HttpURLConnection http) throws IOException{
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
