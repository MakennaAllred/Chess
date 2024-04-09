package dataAccessTests;

import chess.ChessGame;
import dataAccess.*;
import jsonObjects.JoinGameReq;
import jsonObjects.customExceptions.AlreadyTakenException;
import jsonObjects.customExceptions.BadRequestException;
import jsonObjects.customExceptions.DataAccessException;
import jsonObjects.customExceptions.UnauthorizedException;
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

public class DataBaseUnitTests {
    UserData user = new UserData("user","pass","e@mail.com");
    AuthDataAccess authDao = new SQLAuthDao();
    GameDataAccess gameDao = new SQLGameDao();
    UserDataAccess userDao = new SQLUserDao();
    GameService gameService = new GameService(gameDao, authDao);
    UserService userService = new UserService(userDao,authDao);
    ClearService clearService = new ClearService(authDao, userDao, gameDao);
    //Clear test
    @BeforeEach
    public void clearAll() throws UnauthorizedException, DataAccessException {
        clearService.deleteAll();
    }

    // UserDAO tests create, check, delete, get*
    @Test
    //get user and create auth
    public void getUserTestPos() throws BadRequestException, DataAccessException, AlreadyTakenException {
        AuthData auth =  userService.registerUser(user);
        assertEquals(auth.username(), "user");
        assertNotNull(auth.authToken());
    }
    @Test
    public void getUserTestNeg() {
        // empty fields
        assertThrows(BadRequestException.class, () -> userService.registerUser(new UserData(null,"pass","e@mail.com")));
    }

    @Test
    public void createUserTestPos() throws BadRequestException, DataAccessException, AlreadyTakenException {
        AuthData auth =  userService.registerUser(user);
        assertEquals(auth.username(), "user");
        assertNotNull(auth.authToken());
    }

    @Test
    public void createUserTestNeg() {
        // empty fields
        assertThrows(BadRequestException.class, () -> userService.registerUser(new UserData(null,"pass","e@mail.com")));
    }

    @Test
    //check user
    public void checkUserTestPos() throws UnauthorizedException, DataAccessException, BadRequestException, AlreadyTakenException {
        userService.registerUser(user);
        AuthData auth = userService.login(new UserData("user","pass", "e@mail.com"));
        assertNotNull(auth.authToken());
    }

    @Test
    public void checkUserTestNeg(){
        //user didn't register
        assertThrows(UnauthorizedException.class, () -> userService.login(user));
    }


    @Test
    public void deleteAllUsers() throws UnauthorizedException, DataAccessException {
        clearService.deleteAll();
        assertThrows(UnauthorizedException.class, () -> userService.login(user));
    }


    // GameDao create*, get, list*, update*, delete all*
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
        assertEquals(1,games.size());
    }

    @Test
    public void listGamesNeg(){
        assertThrows(UnauthorizedException.class, () -> gameService.listGames("1234"));
    }

    @Test
    public void updateGamePos() throws BadRequestException, DataAccessException, AlreadyTakenException, UnauthorizedException {
        userService.registerUser(user);
        AuthData auth = userService.login(user);
        int gameID = gameService.createGame(auth.authToken(),new GameData(1,null,null, "fakeGame", new ChessGame()));
        gameService.joinGame(auth.authToken(),new JoinGameReq("WHITE", 1));

    }

    @Test
    public void updateGameNeg() throws UnauthorizedException, BadRequestException, DataAccessException, AlreadyTakenException {
        userService.registerUser(user);
        AuthData auth = userService.login(user);
        int gameID = gameService.createGame(auth.authToken(),new GameData(1,null,null, "fakeGame", new ChessGame()));
        assertThrows(BadRequestException.class, () ->gameService.joinGame(auth.authToken(), new JoinGameReq("WHITE", 0)));
    }

    @Test
    public void getGamePos() throws BadRequestException, DataAccessException, AlreadyTakenException, UnauthorizedException {
        userService.registerUser(user);
        AuthData auth = userService.login(user);
        int gameID = gameService.createGame(auth.authToken(),new GameData(1,null,null, "fakeGame", new ChessGame()));
        gameService.joinGame(auth.authToken(),new JoinGameReq("WHITE", 1));

    }

    @Test
    public void getGameNeg() throws UnauthorizedException, BadRequestException, DataAccessException, AlreadyTakenException {
        userService.registerUser(user);
        AuthData auth = userService.login(user);
        int gameID = gameService.createGame(auth.authToken(),new GameData(1,null,null, "fakeGame", new ChessGame()));
        assertThrows(BadRequestException.class, () ->gameService.joinGame(auth.authToken(), new JoinGameReq("WHITE", 0)));
    }
    @Test
    public void deleteAllGamesTest() throws UnauthorizedException, DataAccessException {
        clearService.deleteAll();
        assertEquals(0, gameDao.listGames().size());
    }

    //Auth Dao Tests create*, get*, delete*, deleteall*
    @Test
    public void deleteAuthTestPos() throws UnauthorizedException, DataAccessException, BadRequestException, AlreadyTakenException {
        AuthData auth = userService.registerUser(user);
        assertDoesNotThrow(() -> userService.logout(auth.authToken()));
    }

    @Test
    public void deleteAuthTestNeg(){
        // no auth token
        assertThrows(UnauthorizedException.class,() ->userService.logout(null));
    }
    @Test
    public void deleteAllTest() throws UnauthorizedException, DataAccessException {
        clearService.deleteAll();
        assertThrows(UnauthorizedException.class, () ->userService.login(user));

    }
    @Test
    public void getAuthPos() throws BadRequestException, DataAccessException, AlreadyTakenException, UnauthorizedException {
        AuthData auth = userService.registerUser(user);
        int gameID = gameService.createGame(auth.authToken(),new GameData(1,null,null, "fakeGame", new ChessGame()));
        Collection<GameData> games =  gameService.listGames(auth.authToken());
        assertEquals(1,games.size());
    }
    @Test
    public void getAuthNeg(){
        assertThrows(UnauthorizedException.class, () -> gameService.listGames("1234"));
    }

    @Test
    //get user and create auth
    public void createAuthTestPos() throws BadRequestException, DataAccessException, AlreadyTakenException {
        AuthData auth =  userService.registerUser(user);
        assertNotNull(auth.authToken());
    }
    @Test
    public void createAuthTestNeg() {
        // empty fields
        assertThrows(BadRequestException.class, () -> userService.registerUser(new UserData(null,"pass","e@mail.com")));
    }
}



