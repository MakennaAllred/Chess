package ui;

import chess.*;
import model.GameData;
import ui.webSocket.WebSocketFacade;

import java.util.Arrays;
import java.util.Collection;

public class InGame {
    public static InGameStates state;
    public static String port;
    public static GameData game;

    public static String eval(String port, String input, WebSocketFacade socket) {
        InGame.port = port;
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redrawBoard();
                case "leave" -> leave(socket);
                case "makemove" -> makeMove(socket, params);
                case "resign" -> resign(socket);
                case "highlight" -> highlightLegalMoves(params);
                case "quit" -> "quit";
                default -> Repl.help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    public static String redrawBoard(){
        GenerateBoard.generateBoard(game.game().getTeamTurn(), game.game().getBoard(),null, null);
        return "board drawn";
    }

    public static String leave(WebSocketFacade socket){
        //if observing delete user from both hashmaps and go back to the logged in menu
        socket.leave(Repl.auth.authToken(),Repl.gameID);
        Repl.state = State.SIGNEDIN;
        return "left";
    }
    public static String makeMove(WebSocketFacade socket, String... params){
        //only for players not observers
        if(params.length > 0) {
            ChessMove verified = null;
            int col = params[0].toLowerCase().charAt(0) - 'a' + 1;
            int row = Integer.parseInt(params[0].substring(1));
            ChessPosition start = new ChessPosition(row, col);
            int col1 = params[0].toLowerCase().charAt(0) - 'a' + 1;
            int row1 = Integer.parseInt(params[1].substring(1));
            ChessPosition end = new ChessPosition(row1, col1);
            Collection<ChessMove> valids =  game.game().validMoves(start);
            for(ChessMove pos : valids){
                if(end.getColumn() == pos.getEndPosition().getColumn() && end.getRow() == pos.getEndPosition().getRow()){
                    verified = pos;
                }
            }
            if(verified != null) {
                socket.makeMove(Repl.auth.authToken(), Repl.gameID, verified);
                return "made move";
            }
            else{
                return "not a valid move";
            }
            //allows user to input what move they want to make
            //board updates, and notifies everyone involved in the game
        }
        return "Please type move";
    }
    public static String resign(WebSocketFacade socket){
        //confirms user wants to resign, if yes they lose and game is over
        socket.resign(Repl.auth.authToken(), Repl.gameID);
        return "resigned";
        //notify everyone of other player winning
        //doesn't make resigned user leave
    }
    public static String highlightLegalMoves(String...params){
        //allows user to input what piece they want to know moves for
        //current piece's square and possible squares are highlighted
        if(params.length > 0){
            int col = params[0].toLowerCase().charAt(0) - 'a' + 1;
            int row = Integer.parseInt(params[0].substring(1));
            ChessPosition start = new ChessPosition(row, col);
            Collection<ChessMove> valids = game.game().validMoves(start);
            ChessBoard board = game.game().getBoard();
            GenerateBoard.generateBoard(game.game().getTeamTurn(), board, valids,start);
            if(valids == null){
                System.out.println("No valid moves for this piece");
            }
        }
        return "highlighted moves";
    }

}
