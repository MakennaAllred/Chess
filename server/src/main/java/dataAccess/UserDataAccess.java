package dataAccess;
import model.UserData;


public interface UserDataAccess {
    String createUser(UserData userdata) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    UserData checkUsers(UserData user) throws DataAccessException;

    void deleteAllUsers() throws DataAccessException;
}

