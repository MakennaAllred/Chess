package service;

import dataAccess.*;
import jsonObjects.JoinGameReq;
import jsonObjects.customExceptions.AlreadyTakenException;
import jsonObjects.customExceptions.BadRequestException;
import jsonObjects.customExceptions.DataAccessException;
import jsonObjects.customExceptions.UnauthorizedException;
import model.AuthData;
import model.GameData;
import java.util.Collection;

public class GameService {
    private final GameDataAccess gameDao;
    private final AuthDataAccess authDao;

    public GameService(GameDataAccess gameDao, AuthDataAccess authDao){this.gameDao = gameDao; this.authDao = authDao;}


    public int createGame(String authToken, GameData gameData) throws DataAccessException, UnauthorizedException, BadRequestException {
        AuthData verifiedAuth = authDao.getAuth(authToken);
        if(verifiedAuth != null) {
             return gameDao.createGame(gameData.gameName());
        }
        else{
            throw new UnauthorizedException("Error: Unauthorized");
        }
    }


    public Collection<GameData> listGames(String authToken) throws DataAccessException, UnauthorizedException {
        AuthData auth = authDao.getAuth(authToken);
        if (auth != null) {
            return gameDao.listGames();
        }
        else{
            throw new UnauthorizedException("Error: Can't access games");
        }
    }


    public void joinGame(String authToken, JoinGameReq gameData) throws BadRequestException, UnauthorizedException, DataAccessException, AlreadyTakenException {
        AuthData verifiedUser = authDao.getAuth(authToken);
        if(verifiedUser != null){
            GameData game = gameDao.getGame(gameData.gameID());
            if(game != null) {
                gameDao.updateGame(game.gameID(), verifiedUser.username(), gameData.playerColor());
            }
            else{
                throw new BadRequestException("Error: No game with that ID");
            }
        }
        else{
            throw new UnauthorizedException("Error: Unauthorized");
        }
    }
}


