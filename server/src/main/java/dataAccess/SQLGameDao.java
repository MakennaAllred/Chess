package dataAccess;


import chess.ChessGame;
import com.google.gson.Gson;
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
        String sql = "INSERT INTO users (gameName, game) values (?,?)";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, gameName);
            stmt.setString(2, new Gson().toJson(new ChessGame()));
            stmt.executeUpdate();
        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        Collection<GameData> g = listGames();
        return g.size();
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
                return new GameData(gameId,whiteUser,blackUser,gameName,new Gson().fromJson(game,ChessGame.class));
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
                allGames.add(new GameData(gameId,whiteUser,blackUser,gameName, new Gson().fromJson(game,ChessGame.class)));
            }
            return allGames;
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


    public void updateGame(int gameID, String username, String color) throws DataAccessException, BadRequestException, AlreadyTakenException {
        if(Objects.equals(color, "WHITE")) {
            String statement = "UPDATE games SET whiteUsername = ? WHERE gameID = ?";
            try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(statement)) {
                stmt.setString(1, username);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            String statement = "UPDATE games SET blackUsername = ? WHERE gameID = ?";
            try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(statement)) {
                stmt.setString(1, username);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void deleteAllGames() throws UnauthorizedException, DataAccessException {
        String statement = "DELETE FROM games";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(statement)) {
            stmt.executeUpdate();
            statement = "ALTER TABLE games AUTO_INCREMENT = 1";
            try (PreparedStatement stmt1 = DatabaseManager.getConnection().prepareStatement(statement)) {
                stmt1.executeUpdate();}
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }

    }
}
