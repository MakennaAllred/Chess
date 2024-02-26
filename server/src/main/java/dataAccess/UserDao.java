package dataAccess;

import model.UserData;

import java.util.HashMap;

public class UserDao implements UserDataAccess {
    private int nextId = 1;
    final private HashMap<String, String> users = new HashMap<>();


    @Override
    public UserData createUser(UserData userdata) throws DataAccessException {
        UserData user = new UserData(userdata.username(),userdata.password(),userdata.email());
        users.put(userdata.username(), userdata.password());
        nextId++;
        return null;
    }

    public String getUser(String username){
        return users.get(username);
    }

    @Override
    public void deleteUser(String username) throws DataAccessException {
        users.remove(username);
    }

    public void deleteAllUsers() throws DataAccessException{
        users.clear();
    }
}
