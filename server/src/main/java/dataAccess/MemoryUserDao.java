package dataAccess;

import jsonObjects.customExceptions.DataAccessException;
import jsonObjects.customExceptions.UnauthorizedException;
import model.UserData;

import java.util.HashMap;
import java.util.Objects;

public class MemoryUserDao implements UserDataAccess {

    final private HashMap<String, UserData> users = new HashMap<>();


    public String createUser(UserData userdata) throws DataAccessException {
        UserData user = new UserData(userdata.username(),userdata.password(),userdata.email());
        users.put(userdata.username(), user);
        return userdata.username();
    }

    public String getUser(String username) throws DataAccessException{
        UserData u = users.get(username);
        if(u != null) {
            return u.username();
        }
        else{
            return null;
        }
    }



    public void deleteAllUsers() throws UnauthorizedException {
        users.clear();
    }

    public UserData checkUsers(UserData user) throws DataAccessException, UnauthorizedException {
        UserData memInfo = users.get(user.username());
        if(memInfo != null){
            if(Objects.equals(memInfo.password(), user.password())){
                return memInfo;
            }
            else{
                throw new UnauthorizedException("Error: User Info doesn't match");
            }
        }
        return null;
    }
}
