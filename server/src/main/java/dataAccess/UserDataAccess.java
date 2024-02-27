package dataAccess;
import model.UserData;


public interface UserDataAccess {
    String createUser(UserData userdata) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void deleteUser(String username) throws DataAccessException;

    void deleteAllUsers() throws DataAccessException;
}

