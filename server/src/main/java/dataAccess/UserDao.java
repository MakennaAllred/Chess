package dataAccess;

import model.UserData;

import java.util.HashMap;

public class UserDao implements UserDataAccess {

    final private HashMap<String, UserData> users = new HashMap<>();


    public String createUser(UserData userdata) throws DataAccessException {
        UserData user = new UserData(userdata.username(),userdata.password(),userdata.email());
        users.put(userdata.username(), user);
        return userdata.username();
    }

    public UserData getUser(String username){
        return users.get(username);
    }


    public void deleteUser(String username) throws DataAccessException {
        users.remove(username);
    }

    public void deleteAllUsers() throws DataAccessException{
        users.clear();
    }
}
