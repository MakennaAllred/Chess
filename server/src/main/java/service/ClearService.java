package service;

import dataAccess.*;
import model.AuthData;

public class ClearService {
    private final AuthDao authDao;
    private final UserDao userDao;
    private final GameService gameDao;


    public ClearService(AuthDao authDao, UserDao userDao, GameDao gameDao){this.authDao = authDao; this.userDao = userDao; this.gameDao = gameDao;}


    public void deleteAll() throws DataAccessException {
        authDao.deleteAllTokens();
        userDao.deleteAllUsers();
        gameDao.deleteAllGames();
    }
}
