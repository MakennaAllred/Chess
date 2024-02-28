package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class GameDao implements GameDataAccess{
    private int nextGame = 1;
    final private HashMap<Integer, GameData> games = new HashMap<>();
    public int createGame(String gameName)throws DataAccessException{
        GameData game = new GameData(nextGame, null, null, gameName,new ChessGame());
        nextGame++;
        games.put(game.gameID(),game);
        return game.gameID();
    }

//    public void updateGame(int gameID, String username, String color){
//    }
    public GameData getGame(int gameID){
        return games.get(gameID);
    }
    public Collection<GameData> listGames(){
        return games.values();
    }
    public void updateGame(String username, ChessGame.TeamColor color){
    }

    public void deleteGames(){
        games.clear();
    }
}


//do check before update game, have update game do only one thing
