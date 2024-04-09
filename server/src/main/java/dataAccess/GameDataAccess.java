package dataAccess;

import chess.ChessGame;
import dataAccess.customExceptions.AlreadyTakenException;
import dataAccess.customExceptions.BadRequestException;
import dataAccess.customExceptions.DataAccessException;
import dataAccess.customExceptions.UnauthorizedException;
import model.GameData;

import java.util.Collection;

public interface GameDataAccess {
    public boolean isGameOver = false;


    int createGame(String gameName) throws DataAccessException;
    GameData getGame(int gameID) throws BadRequestException, DataAccessException;

//    void updateGame(int gameID) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    void updateGame(int gameID, String username, String color) throws DataAccessException, BadRequestException, AlreadyTakenException;

    void updateGame(ChessGame game, int gameID);

    void removeUser(GameData game, ChessGame.TeamColor color);

    void deleteAllGames() throws UnauthorizedException, DataAccessException;

}
