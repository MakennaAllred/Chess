package service;

import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import dataAccess.UserDataAccess;

public class AuthService {

    public static void deleteAll() throws DataAccessException {
        AuthDataAccess.deleteAllTokens();
    }
}
