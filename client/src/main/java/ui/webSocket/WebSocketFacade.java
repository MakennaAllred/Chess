package ui.webSocket;

import com.google.gson.Gson;
import com.sun.nio.sctp.NotificationHandler;
import org.eclipse.jetty.io.EndPoint;
import spark.Session;

import javax.management.Notification;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.MessageHandler;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends EndPoint {
    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler){
        try{
            url =url.replace("http", "ws");
            URI socketURI = new URI(url+"/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = (Session) container.connectToServer(this,socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>(){
                @Override
                public void onMessage(String message){
                    Notification notification = new Gson().fromJson(message,Notification.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (URISyntaxException | DeploymentException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}