package dataAccess;
import dataAccess.customExceptions.DataAccessException;
import dataAccess.customExceptions.UnauthorizedException;
import model.UserData;


public interface UserDataAccess {
    String createUser(UserData userdata) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    UserData checkUsers(UserData user) throws DataAccessException, UnauthorizedException;

    void deleteAllUsers() throws UnauthorizedException;
}

