package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDataAccess {


    int createGame(String gameName) throws DataAccessException;
    GameData getGame(int gameID) throws BadRequestException;

//    void updateGame(int gameID) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    void updateGame(int gameID, String username, String color) throws DataAccessException, BadRequestException;

    void deleteAllGames() throws DataAccessException;

}
