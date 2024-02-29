package service;

import dataAccess.*;
import model.AuthData;
import model.GameData;
import java.util.Collection;

public class GameService {
    private final GameDao gameDao;
    private final AuthDao authDao;

    public GameService(GameDao gameDao, AuthDao authDao){this.gameDao = gameDao; this.authDao = authDao;}


    public int createGame(String authToken, GameData gameData) throws DataAccessException, UnauthorizedException, BadRequestException {
        AuthData verifiedAuth = authDao.getAuth(authToken);
        if(verifiedAuth != null) {
             return gameDao.createGame(gameData.gameName());
        }
        else{
            throw new BadRequestException("Error: Unauthorized");
        }
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