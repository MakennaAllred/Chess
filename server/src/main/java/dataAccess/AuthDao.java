package dataAccess;

import model.AuthData;
import model.GameData;

import java.util.HashMap;
import java.util.UUID;

public class AuthDao implements AuthDataAccess{
    final private HashMap<String, AuthData> auths = new HashMap<>();
    public String createAuth(String username){
        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(token, username);
        auths.put(auth.authToken(),auth);
        return auth.authToken();
    }

    public AuthData getAuth(String authToken) throws UnauthorizedException {
        return auths.get(authToken);
    }
    public void deleteAuthToken(String authToken){
        auths.remove(authToken);
    }

    public void deleteAllTokens(){
        auths.clear();
    }
}
