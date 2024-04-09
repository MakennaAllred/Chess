package dataAccess;

import jsonObjects.customExceptions.DataAccessException;
import jsonObjects.customExceptions.UnauthorizedException;
import model.AuthData;

public interface AuthDataAccess {

    AuthData createAuth(String username) throws DataAccessException;

    AuthData getAuth(String authToken) throws UnauthorizedException;

    void deleteAuthToken(String authToken) throws UnauthorizedException;

    void deleteAllTokens() throws UnauthorizedException, DataAccessException;
}
