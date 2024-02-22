package dataAccess;

import chess.ChessGame;

import java.util.ArrayList;
import java.util.Collection;

public class GameDao {

    void createGame(String gameName){

    }
    ChessGame getGame(int gameID){
        ChessGame game = new ChessGame();
        return game;
    }
    Collection<ChessGame> listGames(){
        Collection<ChessGame> total = new ArrayList<>();
        ChessGame game = new ChessGame();
        return total;
    }
    void updateGame(String username, ChessGame.TeamColor color){
    }

    void deleteGames(){}
}
