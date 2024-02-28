package service;

import chess.ChessGame;
import dataAccess.BadRequestException;
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

    public GameData getGame(int gameID) throws BadRequestException {
        return gameDao.getGame(gameID);
    }

    public void updateGame(int gameID, String username, String color) throws DataAccessException{
        gameDao.updateGame(gameID, username,color);
    }
}


// FIXME: rename to memory gameDao