package dataAccess;

import dataAccess.customExceptions.DataAccessException;
import dataAccess.customExceptions.UnauthorizedException;
import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDao implements AuthDataAccess{
    public AuthData createAuth(String username) throws DataAccessException {
        String token = UUID.randomUUID().toString();
        String sql = "INSERT INTO auths (authToken, username) values (?,?)";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, token);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
        return new AuthData(token,username);
    }


    public AuthData getAuth(String authToken) throws UnauthorizedException {
        String statement = "SELECT * FROM auths WHERE authToken = ?";
        try (Connection con = DatabaseManager.getConnection();
                PreparedStatement stmt = con.prepareStatement(statement)) {
            stmt.setString(1,authToken);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                String auth = rs.getString(1);
                String user = rs.getString(2);
                return new AuthData(auth,user);
            }
            else {
                return null;
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteAuthToken(String authToken)throws UnauthorizedException {
        String statement = "DELETE FROM auths WHERE authToken = ?";
        try (Connection con = DatabaseManager.getConnection();
                PreparedStatement stmt = con.prepareStatement(statement)) {
            stmt.setString(1, authToken);
            stmt.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAllTokens() throws UnauthorizedException, DataAccessException {
        String statement = "DELETE FROM auths";
        try (Connection con = DatabaseManager.getConnection();
                PreparedStatement stmt = con.prepareStatement(statement)) {
            stmt.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
