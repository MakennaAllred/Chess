package dataAccess;
import dataAccess.customExceptions.DataAccessException;
import dataAccess.customExceptions.UnauthorizedException;
import model.UserData;



public class SQLUserDao implements UserDataAccess{

    public String createUser(UserData userdata) throws DataAccessException {
        //BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //        String hashedPass = encoder.encode(password);
        //load user, hashedpass, and email into db


        // for login //read previously hashed pass from db
        //        var hashedPass = readHashedPasswordFromDb(username);
        //        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //        return encoder.matches(unhashedPassword, hashedPass);
        return null;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public UserData checkUsers(UserData user) throws DataAccessException, UnauthorizedException {
        return null;
    }

    @Override
    public void deleteAllUsers() throws UnauthorizedException {

    }
}
