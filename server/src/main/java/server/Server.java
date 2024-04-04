package server;

import com.google.gson.Gson;
import dataAccess.*;
import dataAccess.customExceptions.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.webSocket.WebSocketHandler;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.Collection;


public class Server {


    private final UserService userService;
    private final GameService gameService;
    private final ClearService clearService;
    private final WebSocketHandler webSocketHandler;

    public Server() {
        AuthDataAccess authDao = new SQLAuthDao();
        GameDataAccess gameDao = new SQLGameDao();
        UserDataAccess userDao = new SQLUserDao();
        gameService = new GameService(gameDao,authDao);
        userService = new UserService(userDao,authDao);
        clearService = new ClearService(authDao, userDao, gameDao);
        webSocketHandler = new WebSocketHandler();
    }


    public int run(int desiredPort)  {
        Spark.port(desiredPort);
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.createTables();
        }catch(DataAccessException e){
            return -1;
        }

        Spark.staticFiles.location("web");
        Spark.webSocket("/connect", WebSocketHandler.class);

        Spark.init();

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::deleteAll);
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);


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
            clearService.deleteAll();
            res.status(200);
            return "{}";
        }

        catch (UnauthorizedException | DataAccessException e) {
            res.status(401);
            return new  Gson().toJson(new ErrorMessage(e.getMessage()));
        }

    }
    private Object joinGame(Request request, Response response) {
        var game = new Gson().fromJson(request.body(), JoinGameReq.class);
        var authToken = request.headers("authorization");
        try{
            gameService.joinGame(authToken, game);
            response.status(200);
            return "{}";
            }
        catch (DataAccessException e){
            response.status(500);
            return new Gson().toJson(new ErrorMessage(e.getMessage()));
        }
        catch(AlreadyTakenException a){
            response.status(403);
            return new Gson().toJson(new ErrorMessage(a.getMessage()));
        }
        catch(UnauthorizedException u){
            response.status(401);
            return new Gson().toJson(new ErrorMessage(u.getMessage()));

        } catch (BadRequestException b) {
            response.status(400);
            return new Gson().toJson(new ErrorMessage(b.getMessage()));
        }

    }

    private Object createGame(Request request, Response response) {
        var game = new Gson().fromJson(request.body(), GameData.class);
        var auth = request.headers("authorization");
        try{
            int gameID = gameService.createGame(auth, game);
            response.status(200);
            return new Gson().toJson(new CreateGameRes(gameID));
        }
        catch(DataAccessException e){
            response.status(500);
            return new Gson().toJson(new ErrorMessage(e.getMessage()));

        }
        catch(BadRequestException b){
            response.status(400);
        return new Gson().toJson(new ErrorMessage(b.getMessage()));
        }
        catch(UnauthorizedException a){
            response.status(401);
            return new Gson().toJson(new ErrorMessage(a.getMessage()));
        }
    }

    private Object listGames(Request request, Response response) {
        try {
            var auth = request.headers("authorization");
            Collection<GameData> games = gameService.listGames(auth);
            response.status(200);
            return new Gson().toJson(new ListGamesRes(games));
        } catch (DataAccessException e) {
            response.status(500);
            return new Gson().toJson(new ErrorMessage(e.getMessage()));
        } catch (UnauthorizedException a) {
            response.status(401);
            return new Gson().toJson(new ErrorMessage(a.getMessage()));
        }
    }

    private Object logout(Request request, Response response){
        try {
            var auth = request.headers("authorization");
            userService.logout(auth);
            response.status(200);
            return "{}";
        }
        catch(UnauthorizedException u){
            response.status(401);
            return new Gson().toJson(new ErrorMessage(u.getMessage()));
        }
        catch(DataAccessException d){
            response.status(500);
            return new Gson().toJson(new ErrorMessage(d.getMessage()));
        }
    }

    private Object login(Request request, Response response) {
        var user = new Gson().fromJson(request.body(), UserData.class);
        try{
        AuthData auth = userService.login(user);
        response.status(200);
            return new Gson().toJson(auth);
        }
        catch(DataAccessException e){
            response.status(500);
            return new Gson().toJson(new ErrorMessage(e.getMessage()));
        } catch (UnauthorizedException e) {
            response.status(401);
            return new Gson().toJson(new ErrorMessage(e.getMessage()));
        }
    }

    private Object registerUser(Request request, Response response){
       try {
           var user = new Gson().fromJson(request.body(), UserData.class);
           AuthData auth = userService.registerUser(user);
           response.status(200);
           return new Gson().toJson(auth);
       }
       catch(DataAccessException e){
           response.status(500);
           return new Gson().toJson(new ErrorMessage(e.getMessage()));
       } catch (AlreadyTakenException e) {
           response.status(403);
           return new Gson().toJson(new ErrorMessage(e.getMessage()));
       } catch (BadRequestException e) {
           response.status(400);
           return new Gson().toJson(new ErrorMessage(e.getMessage()));
       }
    }
}

