package webSocketMessages.userCommands;

public class Leave extends UserGameCommand{
    public int gameID;
    public Leave(String authToken, CommandType commandType, int gameID) {
        super(authToken);
        this.commandType = CommandType.LEAVE;
        this.gameID = gameID;
    }
}
