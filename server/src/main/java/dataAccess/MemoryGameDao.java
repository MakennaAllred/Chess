package dataAccess;

import chess.ChessGame;
import jsonObjects.customExceptions.AlreadyTakenException;
import jsonObjects.customExceptions.BadRequestException;
import jsonObjects.customExceptions.DataAccessException;
import jsonObjects.customExceptions.UnauthorizedException;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class MemoryGameDao implements GameDataAccess{
    private int nextGame = 1;
    final private HashMap<Integer, GameData> games = new HashMap<>();
    public int createGame(String gameName)throws DataAccessException {
        GameData game = new GameData(nextGame, null, null, gameName,new ChessGame());
        nextGame++;
        games.put(game.gameID(),game);
        return game.gameID();
    }


    public GameData getGame(int gameID)throws BadRequestException {
        return games.get(gameID);
    }
    public Collection<GameData> listGames(){
        return games.values();
    }
    public void updateGame(int gameID, String username, String color) throws DataAccessException, BadRequestException, AlreadyTakenException {
            GameData game = getGame(gameID);
            if(game == null){
                throw new DataAccessException("Error: No Game by that name");
            }
            if (Objects.equals(color, "WHITE")) {
                if(game.whiteUsername() == null) {
                    GameData updated = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
                    games.remove(gameID);
                    games.put(updated.gameID(), updated);
                }
                else{
                    throw new AlreadyTakenException("Error: Already Taken");
                }
            }
            if (Objects.equals(color, "BLACK")) {
                if(game.blackUsername() == null) {
                    GameData updated = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
                    games.remove(gameID);
                    games.put(updated.gameID(), updated);
                }
                else{
                    throw new AlreadyTakenException("Error: Already Taken");
                }
            }

    }

    @Override
    public void updateGame(ChessGame game, int gameID) {

    }

    @Override
    public void removeUser(GameData game, ChessGame.TeamColor color) {

    }

    public void deleteAllGames() throws UnauthorizedException {
        games.clear();
    }
}

