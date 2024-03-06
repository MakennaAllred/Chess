package dataAccess;


import dataAccess.customExceptions.AlreadyTakenException;
import dataAccess.customExceptions.BadRequestException;
import dataAccess.customExceptions.DataAccessException;
import dataAccess.customExceptions.UnauthorizedException;
import model.GameData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class SQLGameDao implements GameDataAccess{

    public int createGame(String gameName)throws DataAccessException {
        String sql = "INSERT INTO users (gameID, whiteUsername, blackUsername, gameName, game) values (?,?,?,?,?)";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            // FIXME:: WHAT GOES IN GAME ID
            stmt.setInt(1, 0);
            stmt.setString(2, "null");
            stmt.setString(3, "null");
            stmt.setString(4, gameName);
            // FIXME:: WHAT METHOD DO YOU USE TO PUT THE GAME INTO THE DB
            stmt.setString(5,game);
            stmt.executeUpdate();

        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        // FIXME::gameid
        return 0;
    }

    public GameData getGame(int gameID) throws BadRequestException, DataAccessException {
        String statement = "SELECT * FROM games WHERE gameId = ?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(statement)) {
            stmt.setInt(1, gameID);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                int gameId = rs.getInt(1);
                String whiteUser = rs.getString(2);
                String blackUser = rs.getString(3);
                String gameName = rs.getString(4);
                String game = rs.getString(5);
                //FIXME:: GAME NEEDS TO BE CHESSGAME BUT IS A STRING
                return new GameData(gameId,whiteUser,blackUser,gameName,game);
            }
            else {
                return null;
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public Collection<GameData> listGames() throws DataAccessException {
        var allGames = new ArrayList<GameData>();
        String statement = "SELECT * FROM games";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(statement)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                int gameId = rs.getInt(1);
                String whiteUser = rs.getString(2);
                String blackUser = rs.getString(3);
                String gameName = rs.getString(4);
                String game = rs.getString(5);
                //FIXME:: GAME NEEDS TO BE CHESSGAME BUT IS A STRING
                allGames.add(new GameData(gameId,whiteUser,blackUser,gameName,game));
            }
            return allGames;
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }


    public void updateGame(int gameID, String username, String color) throws DataAccessException, BadRequestException, AlreadyTakenException {
        //FIXME:: HOW TO SERIALIZE AND DESERIALIZE GAME HERE
    }

    public void deleteAllGames() throws UnauthorizedException, DataAccessException {
        String statement = "DELETE FROM games";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(statement)) {
            stmt.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }

    }
}
