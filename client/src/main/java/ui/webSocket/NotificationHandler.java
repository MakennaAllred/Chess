package ui.webSocket;

import webSocketMessages.serverMessages.*;

public interface NotificationHandler {
    void notify(ServerMessage notification);
}
