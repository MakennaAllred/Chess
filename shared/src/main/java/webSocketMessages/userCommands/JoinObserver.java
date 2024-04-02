package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand{
    public int gameID;

    public JoinObserver(String authToken, CommandType commandType, int gameID) {
        super(authToken);
        this.commandType = CommandType.JOIN_OBSERVER;
        this.gameID = gameID;
    }
}
