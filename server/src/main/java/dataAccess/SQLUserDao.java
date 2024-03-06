package dataAccess;
import dataAccess.customExceptions.DataAccessException;
import dataAccess.customExceptions.UnauthorizedException;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SQLUserDao implements UserDataAccess{

    public String createUser(UserData userdata) throws DataAccessException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPass = encoder.encode(userdata.password());
        String sql = "INSERT INTO users (username, password, email) values (?,?,?)";
        //load user, hashedpass, and email into db
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, userdata.username());
            stmt.setString(2, hashedPass);
            stmt.setString(3, userdata.email());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


        public UserData getUser(String username) throws DataAccessException {
            // for login //read previously hashed pass from db
            String statement = "SELECT FROM users WHERE username = ?";
//            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//            return encoder.matches(unhashedPassword, hashedPass);
//            return users.get(username);
            try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(statement)) {
                //fix ?
                stmt.setString(1,username);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                    String uname = rs.getString(1);
                }
                else {
                    return null;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

    public void deleteAllUsers() throws UnauthorizedException {
        String statement = "DELETE FROM users";
    }

    public UserData checkUsers(UserData user) throws DataAccessException, UnauthorizedException {
        return null;
        }

}
