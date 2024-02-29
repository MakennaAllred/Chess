package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;

public class UserService {

    private final UserDataAccess userDao;
    private final AuthDataAccess authDao;

    public UserService(UserDataAccess userDao, AuthDataAccess authDao){this.userDao = userDao; this.authDao=authDao;}
    public AuthData registerUser(UserData user)throws DataAccessException{
        if (userDao.getUser(user.username()) == null){
            String username = userDao.createUser(user);
            return authDao.createAuth(username);
        }
        else{
            throw new DataAccessException("Error: no user created");
        }
    }

    public AuthData login(UserData user)throws DataAccessException{
       UserData authenticatedUser = userDao.checkUsers(user);
        if (authenticatedUser != null){
           return authDao.createAuth(authenticatedUser.username());
       }
        else{
            throw new DataAccessException("Error: Couldn't log user in");
        }
    }

    public void logout(String authToken) throws DataAccessException, UnauthorizedException {
        AuthData auth = authDao.getAuth(authToken);
        if(auth != null){
            authDao.deleteAuthToken(authToken);
        }
        else{
            throw new DataAccessException("Error: No token to delete");
        }
    }

}
