package webSocketMessages.serverMessages;

import webSocketMessages.userCommands.UserGameCommand;

public class Notification extends ServerMessage {
    public String message;
    public Notification(ServerMessageType type, String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }
}
