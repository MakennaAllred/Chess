package service;

import dataAccess.DataAccessException;
import dataAccess.GameDataAccess;
import model.GameData;
import java.util.Collection;

public class GameService {
    private final GameDataAccess gameDataAccess;

    public GameService(GameDataAccess gameDataAccess){this.gameDataAccess = gameDataAccess;}

    public void deleteAll() throws DataAccessException {
        gameDataAccess.deleteGames();
    }
    public Collection<GameData> listGames() throws DataAccessException{
        return GameDataAccess.listGames();
    }
}


// FIXME: rename to memory gameDao