package service;

import dataAccess.DataAccessException;
import dataAccess.GameDao;
import dataAccess.UserDao;
import dataAccess.UserDataAccess;
import model.AuthData;
import model.UserData;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao){this.userDao = userDao;}
    public String registerUser(UserData user)throws DataAccessException{
        return userDao.createUser(user);
    }
    public UserData getUser(String username) throws DataAccessException{
        return userDao.getUser(username);
    }
    public AuthData login(UserData user)throws DataAccessException{
        return null;
    }
    public void logout(UserData user) throws DataAccessException{}

    public void deleteAll() throws DataAccessException {
        userDao.deleteAllUsers();
    }
}
