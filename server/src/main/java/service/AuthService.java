package service;

import dataAccess.*;
import model.AuthData;

public class AuthService {
    private final AuthDao authDao;

    public AuthService(AuthDao authDao){this.authDao = authDao;}

    public AuthData getAuth(String authToken)throws UnauthorizedException{
        return authDao.getAuth(authToken);
    }

    public AuthData createAuth(String username) throws DataAccessException{
        return new AuthData(authDao.createAuth(username), username) ;
    }

    public void deleteAuthToken(String authToken) throws DataAccessException{
        authDao.deleteAuthToken(authToken);
    }

    public void deleteAll() throws DataAccessException {
        authDao.deleteAllTokens();
    }
}
