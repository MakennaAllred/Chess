package service;

import dataAccess.DataAccessException;
import dataAccess.UserDataAccess;
import model.AuthData;
import model.UserData;

public class UserService {
    public AuthData registerUser(UserData user){
        return null;
    }
    public AuthData login(UserData user){
        return null;
    }
    public void logout(UserData user){}

    public static void deleteAll() throws DataAccessException {
        UserDataAccess.deleteAllUsers();
    }
}
