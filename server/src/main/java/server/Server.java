package server;

import dataAccess.DataAccessException;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;

import javax.xml.crypto.Data;


public class Server {

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
        UserService.deleteAll();
        GameService.deleteAll();
        AuthService.deleteAll();
        res.status(200);
        return "";
    }
    private Object joinGame(Request request, Response response) {
    }

    private Object createGame(Request request, Response response) {
    }

    private Object listGames(Request request, Response response) {
        response.type("application/json");
        var list = GameService.
    }

    private Object logout(Request request, Response response) {
    }

    private Object login(Request request, Response response) {
    }

    private Object registerUser(Request request, Response response) throws DataAccessException {
    }
}
