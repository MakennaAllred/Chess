package service;

import dataAccess.*;
import jsonObjects.customExceptions.DataAccessException;
import jsonObjects.customExceptions.UnauthorizedException;

public class ClearService {
    private final AuthDataAccess authDao;
    private final UserDataAccess userDao;
    private final GameDataAccess gameDao;


    public ClearService(AuthDataAccess authDao, UserDataAccess userDao, GameDataAccess gameDao){this.authDao = authDao; this.userDao = userDao; this.gameDao = gameDao;}


    public void deleteAll() throws UnauthorizedException, DataAccessException {
        authDao.deleteAllTokens();
        userDao.deleteAllUsers();
        gameDao.deleteAllGames();
    }
}
