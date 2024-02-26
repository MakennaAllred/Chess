package dataAccess;
import model.UserData;


public interface UserDataAccess {
    UserData createUser(UserData userdata) throws DataAccessException;

    String getUser(String username) throws DataAccessException;

    void deleteUser(String username) throws DataAccessException;

    void deleteAllUsers() throws DataAccessException;
}

