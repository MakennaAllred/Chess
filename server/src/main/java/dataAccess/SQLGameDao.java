package dataAccess;

import chess.ChessGame;
import dataAccess.customExceptions.AlreadyTakenException;
import dataAccess.customExceptions.BadRequestException;
import dataAccess.customExceptions.DataAccessException;
import dataAccess.customExceptions.UnauthorizedException;
import model.GameData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

public class SQLGameDao implements GameDataAccess{

    public int createGame(String gameName)throws DataAccessException {
        String sql = "INSERT INTO users (gameID, whiteUsername, blackUsername, gameName, game) values (?,?,?,?,?)";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            // FIXME:: WHAT GOES IN GAME ID
            stmt.setString(1, "null");
            stmt.setString(2, "null");
            stmt.executeUpdate();

        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }


        GameData game = new GameData(nextGame, null, null, gameName,new ChessGame());
        nextGame++;
        games.put(game.gameID(),game);
        return game.gameID();
    }

    public GameData getGame(int gameID)throws BadRequestException {
        return games.get(gameID);
    }

    public Collection<GameData> listGames(){
        return games.values();
    }


    public void updateGame(int gameID, String username, String color) throws DataAccessException, BadRequestException, AlreadyTakenException {
        GameData game = getGame(gameID);
        if(game == null){
            throw new DataAccessException("Error: No Game by that name");
        }
        if (Objects.equals(color, "WHITE")) {
            if(game.whiteUsername() == null) {
                GameData updated = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
                games.remove(gameID);
                games.put(updated.gameID(), updated);
            }
            else{
                throw new AlreadyTakenException("Error: Already Taken");
            }
        }
        if (Objects.equals(color, "BLACK")) {
            if(game.blackUsername() == null) {
                GameData updated = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
                games.remove(gameID);
                games.put(updated.gameID(), updated);
            }
            else{
                throw new AlreadyTakenException("Error: Already Taken");
            }
        }

    }

    public void deleteAllGames() throws UnauthorizedException {
        games.clear();
    }
}
