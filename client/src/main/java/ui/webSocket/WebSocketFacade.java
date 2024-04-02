package ui.webSocket;

import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    public Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler){
        try{
            url =url.replace("http", "ws");
            URI socketURI = new URI(url+"/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this,socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>(){
                @Override
                public void onMessage(String message){
                    UserGameCommand notification = new Gson().fromJson(message,UserGameCommand.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (URISyntaxException | DeploymentException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onOpen(javax.websocket.Session session, EndpointConfig endpointConfig) {

    }
}