package clientTests;

import chess.ChessGame;
import dataAccess.*;
import dataAccess.customExceptions.AlreadyTakenException;
import dataAccess.customExceptions.BadRequestException;
import dataAccess.customExceptions.DataAccessException;
import dataAccess.customExceptions.UnauthorizedException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;


import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {
    static UserData user = new UserData("user","pass","e@mail.com");
    public static AuthData auth;
    private static Server server;
    public static int port;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void clear(){
        auth = new ServerFacade(port).register(new UserData("z","b","c"));
        new ServerFacade(port).deleteAll(auth.authToken());
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }


    // User Service tests
    @Test
    public void registerTestPos() throws BadRequestException, DataAccessException, AlreadyTakenException {
        auth =  new ServerFacade(port).register(user);
        assertEquals(auth.username(), "user");
        assertNotNull(auth.authToken());
    }
    @Test
    public void registerTestNeg() {
        // empty fields
        assertThrows(RuntimeException.class, () -> new ServerFacade(port).register(new UserData(null,"pass","e@mail.com")));
    }

    @Test
    public void loginTestPos() throws UnauthorizedException, DataAccessException, BadRequestException, AlreadyTakenException {
        auth =  new ServerFacade(port).register(user);
        auth = new ServerFacade(port).login(user);
        assertNotNull(auth.authToken());
    }

    @Test
    public void loginTestNeg(){
        //user didn't register
        assertThrows(RuntimeException.class, () -> new ServerFacade(port).login(new UserData("a", "b", "c")));
    }

    @Test
    public void logoutTestPos() throws UnauthorizedException, DataAccessException, BadRequestException, AlreadyTakenException {
        auth =  new ServerFacade(port).register(user);
        auth = new ServerFacade(port).login(user);
        assertDoesNotThrow(() -> new ServerFacade(port).logout(auth.authToken()));
    }

    @Test
    public void logoutTestNeg(){
        // no auth token
        assertThrows(RuntimeException.class,() -> new ServerFacade(port).logout(null));
    }


    // Game Service Tests
    @Test
    public void createGameNeg() throws UnauthorizedException, BadRequestException, DataAccessException {
        //didn't log in
        assertThrows(RuntimeException.class, () ->new ServerFacade(port).createGame("1234",new GameData(1,null,null, "fakeGame", new ChessGame())));

    }
    @Test
    public void createGamePost() throws BadRequestException, DataAccessException, AlreadyTakenException, UnauthorizedException {
        AuthData auth = new ServerFacade(port).register(user);
        CreateGameRes game = new ServerFacade(port).createGame(auth.authToken(),new GameData(1,null,null, "fakeGame", new ChessGame()));
        assertEquals(game.gameID(),1);
    }

    @Test
    public void listGamesPos() throws BadRequestException, DataAccessException, AlreadyTakenException, UnauthorizedException {
        auth = new ServerFacade(port).register(user);
        auth = new ServerFacade(port).login(user);
        CreateGameRes game = new ServerFacade(port).createGame(auth.authToken(),new GameData(1,null,null, "fakeGame", new ChessGame()));
        ListGamesRes games =  new ServerFacade(port).listGames(auth.authToken());
        assertEquals(1,games.games().size());
    }

    @Test
    public void listGamesNeg(){
        assertThrows(RuntimeException.class, () -> new ServerFacade(port).listGames("1234"));
    }

    @Test
    public void joinGamePos() throws BadRequestException, DataAccessException, AlreadyTakenException, UnauthorizedException {
        auth = new ServerFacade(port).register(user);
        auth = new ServerFacade(port).login(user);
        CreateGameRes game = new ServerFacade(port).createGame(auth.authToken(),new GameData(1,null,null, "fakeGame", new ChessGame()));
        new ServerFacade(port).joinGame(auth.authToken(),new JoinGameReq("WHITE", 1));

    }

    @Test
    public void joinGameNeg() throws UnauthorizedException, BadRequestException, DataAccessException, AlreadyTakenException {
        auth = new ServerFacade(port).register(user);
        auth = new ServerFacade(port).login(user);
        CreateGameRes game = new ServerFacade(port).createGame(auth.authToken(),new GameData(1,null,null, "fakeGame", new ChessGame()));
        assertThrows(RuntimeException.class, () -> new ServerFacade(port).joinGame(auth.authToken(), new JoinGameReq("WHITE", 0)));
    }
    @Test
    public void clearAllTest() throws UnauthorizedException, DataAccessException {
        auth = new ServerFacade(port).register(new UserData("z","b","c"));
        new ServerFacade(port).deleteAll(auth.authToken());
        assertThrows(RuntimeException.class, () -> new ServerFacade(port).login(new UserData("a", "b", "c")));
    }
}





