package dataAccess;
import jsonObjects.customExceptions.DataAccessException;
import jsonObjects.customExceptions.UnauthorizedException;
import model.UserData;


public interface UserDataAccess {
    String createUser(UserData userdata) throws DataAccessException;

    String getUser(String username) throws DataAccessException;

    UserData checkUsers(UserData user) throws DataAccessException, UnauthorizedException;

    void deleteAllUsers() throws UnauthorizedException, DataAccessException;
}

