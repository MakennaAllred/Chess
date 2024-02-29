package serviceTests;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.GameService;
import service.UserService;

import java.util.Collection;


import static org.junit.jupiter.api.Assertions.*;

public class UnitTests {
    UserData user = new UserData("user","pass","e@mail.com");
    AuthDataAccess authDao = new AuthDao();
    GameDataAccess gameDao = new GameDao();
    UserDataAccess userDao = new UserDao();
    GameService gameService = new GameService(gameDao, authDao);
    UserService userService = new UserService(userDao,authDao);
    ClearService clearService = new ClearService(authDao, userDao, gameDao);
//Clear test
    @BeforeEach
    public void clearAll() throws UnauthorizedException {
        clearService.deleteAll();
    }

// User Service tests
@Test
    public void registerTestPos() throws BadRequestException, DataAccessException, AlreadyTakenException {
        AuthData auth =  userService.registerUser(user);
        assertEquals(auth.username(), "user");
        assertNotNull(auth.authToken());
    }
    @Test
    public void registerTestNeg() {
        // empty fields
        assertThrows(BadRequestException.class, () -> userService.registerUser(new UserData(null,"pass","e@mail.com")));
    }

    @Test
    public void loginTestPos() throws UnauthorizedException, DataAccessException, BadRequestException, AlreadyTakenException {
        userService.registerUser(user);
        AuthData auth = userService.login(new UserData("user","pass", "e@mail.com"));
        assertNotNull(auth.authToken());
    }

    @Test
    public void loginTestNeg(){
        //user didn't register
        assertThrows(UnauthorizedException.class, () -> userService.login(user));
    }

    @Test
    public void logoutTestPos() throws UnauthorizedException, DataAccessException, BadRequestException, AlreadyTakenException {
        AuthData auth = userService.registerUser(user);
        assertDoesNotThrow(() -> userService.logout(auth.authToken()));
    }

    @Test
    public void logoutTestNeg(){
        // no auth token
        assertThrows(UnauthorizedException.class,() ->userService.logout(null));
    }


    // Game Service Tests
    @Test
    public void createGameNeg() throws UnauthorizedException, BadRequestException, DataAccessException {
        //didn't log in
        assertThrows(UnauthorizedException.class, () ->gameService.createGame("1234",new GameData(1,null,null, "fakeGame", new ChessGame())));

    }
    @Test
    public void createGamePost() throws BadRequestException, DataAccessException, AlreadyTakenException, UnauthorizedException {
        AuthData auth = userService.registerUser(user);
        int gameID = gameService.createGame(auth.authToken(),new GameData(1,null,null, "fakeGame", new ChessGame()));
        assertEquals(gameID,1);
    }

    @Test
    public void listGamesPos() throws BadRequestException, DataAccessException, AlreadyTakenException, UnauthorizedException {
        AuthData auth = userService.registerUser(user);
        int gameID = gameService.createGame(auth.authToken(),new GameData(1,null,null, "fakeGame", new ChessGame()));
        Collection<GameData> games =  gameService.listGames(auth.authToken());
        assertEquals(0,games.size());
    }

    @Test
    public void listGamesNeg(){
        assertThrows(UnauthorizedException.class, () -> gameService.listGames("1234"));
    }

    @Test
    public void joinGamePos() throws BadRequestException, DataAccessException, AlreadyTakenException, UnauthorizedException {
        userService.registerUser(user);
        AuthData auth = userService.login(user);
        int gameID = gameService.createGame(auth.authToken(),new GameData(1,null,null, "fakeGame", new ChessGame()));
        gameService.joinGame(auth.authToken(),new JoinGameReq("WHITE", 1));

    }

    @Test
    public void joinGameNeg() throws UnauthorizedException, BadRequestException, DataAccessException, AlreadyTakenException {
        userService.registerUser(user);
        AuthData auth = userService.login(user);
        int gameID = gameService.createGame(auth.authToken(),new GameData(1,null,null, "fakeGame", new ChessGame()));
        assertThrows(BadRequestException.class, () ->gameService.joinGame(auth.authToken(), new JoinGameReq("WHITE", 0)));
    }
}


//list games, what do I put in the equals
//joinGamePos, logoutPos how do I check
