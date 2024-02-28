package server;

import com.google.gson.Gson;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;

import java.io.Reader;


public class Server {


    private final UserService userService;
    private final GameService gameService;
    private final AuthService authService;

    public Server() {
        gameService = new GameService(new GameDao());
        userService = new UserService(new UserDao());
        authService = new AuthService(new AuthDao());
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        Spark.init();
        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::deleteAll);
//        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::login);
//        Spark.delete("/session", this::logout);
//        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
//        Spark.put("/game", this::joinGame);
//

        Spark.awaitInitialization();
        return Spark.port();
    }



    public static void main(String[] args){
        Server server = new Server();
        server.run(8080);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object deleteAll(Request req, Response res) {
        try {
            userService.deleteAll();
            gameService.deleteAll();
            authService.deleteAll();
            res.status(200);
            return "{}";
        }
        catch(DataAccessException e){
            res.status(500);
            return new Gson().toJson(ErrorMessage.class);
        }

    }
    private Object joinGame(Request request, Response response) throws DataAccessException{
        var game = new Gson().fromJson(request.body(), GameData.class);
        var auth = request.headers("authorization");
        try{
            String username =  userService.registerUser();
            authService.createAuth(username);
       }


    }

    private Object createGame(Request request, Response response) {
        var game = new Gson().fromJson(request.body(), GameData.class);
        var auth = request.headers("authorization");
        try{
            authService.getAuth(auth);
            int gameID = gameService.createGame(game.gameName());
            response.status(200);
            return new Gson().toJson(new CreateGameRes(gameID));
        }
        catch(DataAccessException e){
            response.status(500);
            return new Gson().toJson(ErrorMessage.class);

        }
//        catch(BadRequestException b){
//            response.status(400);
//            return new Gson().toJson(ErrorMessage.class);
//        }
        catch(UnauthorizedException a){
            response.status(401);
            return new Gson().toJson(ErrorMessage.class);
        }
    }
//
//    private Object listGames(Request request, Response response) {
//
//
//    }
//
//    private Object logout(Request request, Response response) throws DataAccessException {
//        AuthData authDetails = authService.getAuth(authToken);
//        authService.deleteAuthToken(authDetails.authToken());
//    }

    private Object login(Request request, Response response) {
        var user = new Gson().fromJson(request.body(), UserData.class);
        try{
        AuthData auth = authService.createAuth(user.username());
        response.status(200);
            return new Gson().toJson(auth);
        }
        catch(DataAccessException e){
            response.status(500);
            return new Gson().toJson(ErrorMessage.class);
        }
    }
//
//    private Object registerUser(Request request, Response response) throws DataAccessException {
//        userService.checkUser();
//        String username = userService.registerUser();
//        authService.createAuth(username);
//
//    }
}

// gotta set res.status and return json to gson stuff
// create record for need request and response classes
//make update game in dao, get game, set username, and put back