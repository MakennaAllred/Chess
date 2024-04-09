package dataAccess;

import jsonObjects.customExceptions.UnauthorizedException;
import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDao implements AuthDataAccess{
    final private HashMap<String, AuthData> auths = new HashMap<>();
    public AuthData createAuth(String username){
        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(token, username);
        auths.put(auth.authToken(),auth);
        return auth;
    }

    public AuthData getAuth(String authToken) throws UnauthorizedException {
        return auths.get(authToken);
    }
    public void deleteAuthToken(String authToken)throws UnauthorizedException{
        auths.remove(authToken);
    }

    public void deleteAllTokens() throws UnauthorizedException{
        auths.clear();
    }
}
