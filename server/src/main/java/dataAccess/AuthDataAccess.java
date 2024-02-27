package dataAccess;

import model.AuthData;

public interface AuthDataAccess {

    String createAuth(String username) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void deleteAuthToken(String authToken) throws DataAccessException;

    static void deleteAllTokens() throws DataAccessException;
}
