package ui.webSocket;

import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.UserGameCommand;

public interface NotificationHandler {
    void notify(String notification);
}
