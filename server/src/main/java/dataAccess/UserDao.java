package dataAccess;

import model.UserData;

import java.util.HashMap;
import java.util.Objects;

public class UserDao implements UserDataAccess {

    final private HashMap<String, UserData> users = new HashMap<>();


    public String createUser(UserData userdata) throws DataAccessException {
        UserData user = new UserData(userdata.username(),userdata.password(),userdata.email());
        users.put(userdata.username(), user);
        return userdata.username();
    }

    public UserData getUser(String username) throws DataAccessException{
        return users.get(username);
    }


    public void deleteUser(String username) throws DataAccessException {
        users.remove(username);
    }

    public void deleteAllUsers() throws DataAccessException{
        users.clear();
    }

    public UserData checkUsers(UserData user) throws DataAccessException{
        UserData memInfo = users.get(user.username());
        if(memInfo != null){
            if(Objects.equals(memInfo.password(), user.password())){
                return memInfo;
            }
            else{
                throw new DataAccessException("Error: User Info doesn't match");
            }
        }else{
            throw new DataAccessException("Error: No user");
        }
    }
}
