package webSocketMessages.userCommands;

public class Resign extends UserGameCommand{
    public int gameID;
    public Resign(String authToken, CommandType commandType, int gameID) {
        super(authToken);
        this.commandType = CommandType.RESIGN;
        this.gameID = gameID;
    }
}
