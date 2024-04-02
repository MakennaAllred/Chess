package server.webSocket;
import javax.websocket.*;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import webSocketMessages.userCommands.UserGameCommand;


import java.io.IOException;

public class WebSocketHandler {
    private final ConnectionsManager connections = new ConnectionsManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException{
        UserGameCommand userGameCommand = new Gson().fromJson(msg,UserGameCommand.class);
        switch(userGameCommand.getCommandType()){
            case JOIN_PLAYER -> joinPlayer();
            case JOIN_OBSERVER -> joinObserver();
            case LEAVE -> leave();
            case RESIGN -> resign();
            case MAKE_MOVE -> makeMove();
        }
    }

    private void joinPlayer(){
        //connections.add()
        String message = String.format("%s joined as %d")
        //server sends LOAD GAME msg back to root client
        // server sends NOTIFICATION msg to other clients in game notifying what color root client is joining as
    }
    private void joinObserver(){
        //sends load game message back to root client
        //sends NOTIFICATION msg to other clients in that game informing root joined as observer
    };
    private void leave(){
        //game is updated to remove root client, game is updated in db
        //server sends NOTIFICATION message to other clients informing them root left
    };
    private void resign(){
        //server marks game as game over(no more moves can be made)
        //game is updated in db
        //server sends NOTIFICATION message to all clients that root left
    };
    private void makeMove(){
        //verify valid move
        //game is updated to represent the move and game is updated in db
        //server sends LOAD GAME to all clients in game (including root) with updated game
        //server sends NOTIFICATION msg to other clients informing them what move was made
    };
}
