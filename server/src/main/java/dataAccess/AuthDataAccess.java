package dataAccess;

import model.AuthData;

public interface AuthDataAccess {

    AuthData createAuth(String username) throws DataAccessException;

    AuthData getAuth(String authToken) throws UnauthorizedException;

    void deleteAuthToken(String authToken) throws DataAccessException;

    void deleteAllTokens() throws DataAccessException;
}
