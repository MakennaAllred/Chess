package server.webSocket;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;



public class ConnectionsManager {
    public final HashMap<String, Connection> authsAndSessions = new HashMap<>();

    public void add(String authToken, Session session) {
        var connection = new Connection(authToken, session);
        authsAndSessions.put(authToken, connection);
    }

    public void remove(String authToken) {
        authsAndSessions.remove(authToken);
    }

    public void broadcast(String excludeAuthToken, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : authsAndSessions.values()) {
            if (c.session.isOpen()) {
                if (!c.authToken.equals(excludeAuthToken)) {
                    c.send(notification.toString());
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            authsAndSessions.remove(c.authToken);
        }
    }
    public void clientNotify(String authToken, ServerMessage notification) throws IOException {
        for (var c : authsAndSessions.values()) {
            if (c.session.isOpen()) {
                if (c.authToken.equals(authToken)) {
                    c.send(notification.toString());
                    return;
                }
            }
        }
    }
}
