package server;

import dataAccess.AuthDao;
import dataAccess.DataAccessException;
import dataAccess.GameDao;
import dataAccess.UserDao;
import model.AuthData;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;

import javax.xml.crypto.Data;


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

    private Object deleteAll(Request req, Response res) throws DataAccessException {
        userService.deleteAll();
        gameService.deleteAll();
        authService.deleteAll();
        res.status(200);
        return "";
    }
    private Object joinGame(Request request, Response response) throws DataAccessException{
//       String username =  userService.registerUser();
//       authService.createAuth(username);
//       FIXME::figure out update game method and get game from diagram
    }

    private Object createGame(Request request, Response response) {
    }

    private Object listGames(Request request, Response response) {
        response.type("application/json");

    }

    private Object logout(Request request, Response response) throws DataAccessException {
        AuthData authDetails = authService.getAuth(authToken);
        authService.deleteAuthToken(authDetails.authToken());
    }

    private Object login(Request request, Response response) throws  DataAccessException{
        //FIXME::check if user exists
        authService.createAuth(username);
    }

    private Object registerUser(Request request, Response response) throws DataAccessException {
        userService.getUser();
        String username = userService.registerUser();
        authService.createAuth(username);

    }
}
