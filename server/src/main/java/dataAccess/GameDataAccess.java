package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDataAccess {


    int createGame(String gameName) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;

//    void updateGame(int gameID) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    void updateGame(String username, ChessGame.TeamColor color) throws DataAccessException;

    void deleteGames() throws DataAccessException;

}
