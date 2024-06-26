package dataAccess;
import jsonObjects.customExceptions.DataAccessException;
import jsonObjects.customExceptions.UnauthorizedException;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SQLUserDao implements UserDataAccess{

    public String createUser(UserData userdata) throws DataAccessException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPass = encoder.encode(userdata.password());
        String sql = "INSERT INTO users (username, password, email) values (?,?,?)";
        try (Connection con = DatabaseManager.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, userdata.username());
            stmt.setString(2, hashedPass);
            stmt.setString(3, userdata.email());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return userdata.username();
    }


        public String getUser(String username) throws DataAccessException {
            String statement = "SELECT * FROM users WHERE username = ?";
            try (Connection con = DatabaseManager.getConnection();
                    PreparedStatement stmt = con.prepareStatement(statement)) {
                stmt.setString(1,username);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                   return rs.getString(1);
                }
                else {
                    return null;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    public void deleteAllUsers() throws UnauthorizedException, DataAccessException {
        String statement = "DELETE FROM users";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(statement)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public UserData checkUsers(UserData user) throws DataAccessException, UnauthorizedException {
        String statement = "SELECT * FROM users WHERE username = ?";
        try (Connection con = DatabaseManager.getConnection();
                PreparedStatement stmt = con.prepareStatement(statement)) {
            stmt.setString(1,user.username());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                String name = rs.getString(1);
                String pass = rs.getString(2);
                String email = rs.getString(3);
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                boolean match =  encoder.matches(user.password(), pass);
                if(match){
                    return new UserData(name,pass, email);
                }
                else{
                    throw new UnauthorizedException("Error: Passwords don't match");
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
            }
        return null;
        }

}
