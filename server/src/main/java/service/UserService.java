package service;

import dataAccess.*;
import dataAccess.customExceptions.AlreadyTakenException;
import dataAccess.customExceptions.BadRequestException;
import dataAccess.customExceptions.DataAccessException;
import dataAccess.customExceptions.UnauthorizedException;
import model.AuthData;
import model.UserData;

public class UserService {

    private final UserDataAccess userDao;
    private final AuthDataAccess authDao;

    public UserService(UserDataAccess userDao, AuthDataAccess authDao){this.userDao = userDao; this.authDao=authDao;}
    public AuthData registerUser(UserData user) throws DataAccessException, AlreadyTakenException, BadRequestException {
        if(user.password() == null || user.username() == null || user.email() == null){
            throw new BadRequestException("Error: Need to fill in fields");
        }
        if (userDao.getUser(user.username()) == null){
            String username = userDao.createUser(user);
            return authDao.createAuth(username);
        }
        else{
            throw new AlreadyTakenException("Error: Already Taken");
        }
    }

    public AuthData login(UserData user) throws DataAccessException, UnauthorizedException {
       UserData authenticatedUser = userDao.checkUsers(user);
        if (authenticatedUser != null){
           return authDao.createAuth(authenticatedUser.username());
       }
        else{
            throw new UnauthorizedException("Error: Unauthorized");
        }
    }

    public void logout(String authToken) throws DataAccessException, UnauthorizedException {
        AuthData auth = authDao.getAuth(authToken);
        if(auth != null){
            authDao.deleteAuthToken(authToken);
        }
        else{
            throw new UnauthorizedException("Error: No user to logout");
        }
    }

}
