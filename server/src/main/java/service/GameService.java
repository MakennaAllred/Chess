package service;

import dataAccess.DataAccessException;
import dataAccess.GameDao;
import model.GameData;
import java.util.Collection;

public class GameService {
    private final GameDao gameDao;

    public GameService(GameDao gameDao){this.gameDao = gameDao;}

    public void deleteAll() throws DataAccessException {
        gameDao.deleteGames();
    }

    public int createGame(String gameName) throws DataAccessException{
        return gameDao.createGame(gameName);
    }


    public Collection<GameData> listGames() throws DataAccessException{
        return gameDao.listGames();
    }
}


// FIXME: rename to memory gameDao