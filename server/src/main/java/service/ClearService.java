package service;

import dataAccess.*;
import model.AuthData;

public class ClearService {
    private final AuthDataAccess authDao;
    private final UserDataAccess userDao;
    private final GameDataAccess gameDao;


    public ClearService(AuthDataAccess authDao, UserDataAccess userDao, GameDataAccess gameDao){this.authDao = authDao; this.userDao = userDao; this.gameDao = gameDao;}


    public void deleteAll() throws DataAccessException {
        authDao.deleteAllTokens();
        userDao.deleteAllUsers();
        gameDao.deleteAllGames();
    }
}
