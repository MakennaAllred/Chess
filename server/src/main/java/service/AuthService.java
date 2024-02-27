package service;

import dataAccess.*;
import model.AuthData;

public class AuthService {
    private final AuthDao authDao;

    public AuthService(AuthDao authDao){this.authDao = authDao;}

    public AuthData getAuth(String authToken)throws DataAccessException{
        return authDao.getAuth(authToken);
    }

    public String createAuth(String username) throws DataAccessException{
        return authDao.createAuth(username);
    }

    public void deleteAuthToken(String authToken) throws DataAccessException{
        authDao.deleteAuthToken(authToken);
    }

    public void deleteAll() throws DataAccessException {
        authDao.deleteAllTokens();
    }
}
