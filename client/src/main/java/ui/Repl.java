package ui;

import chess.ChessGame;
import model.AuthData;
import ui.webSocket.NotificationHandler;
import ui.webSocket.WebSocketFacade;
import webSocketMessages.serverMessages.ServerMessage;

import java.net.http.WebSocket;
import java.util.HashMap;
import java.util.Scanner;

public class Repl implements NotificationHandler {
    public static State state = State.SIGNEDOUT;
    public static HashMap<String, WebSocket> authsAndSessions = new HashMap<String,WebSocket>();
    public static HashMap <Integer, String> gameIDAndUsers = new HashMap<Integer, String>();
    public static AuthData auth;
    public static String username;
    public static String serverURL;
    public static ChessGame game;
    public static int gameID;
    public WebSocketFacade socket;

    public Repl(int port){
        String p = String.valueOf(port);
        serverURL = "http://localhost:" + p;
        socket = new WebSocketFacade(serverURL, this);
    }
    public void run(){
        System.out.println("Welcome to Chess, login to start.");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        Object res = "";
        while(res == null || !res.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();

            try{
                if(state == State.SIGNEDOUT){
                    res = PreLoginMenu.eval(serverURL, line);
                } else if (state == State.SIGNEDIN) {
                    res = PostLoginMenu.eval(serverURL, line, socket);
                }else{
                    res = InGame.eval(serverURL,line, socket);
                }

            }
            catch(Throwable e){
                e.printStackTrace();
                System.out.print(e.getMessage());
            }
        }
        System.out.println();
    }

    private void printPrompt(){System.out.print("\n" + ">>>");}

    public String help(){
        if(state == State.SIGNEDOUT){
            return """
                    - Register <username password email>
                    - Login <username password>
                    - Quit
                    """;
        }
        return """
                - Create <gameName>
                - Join <PlayerColor gameID>
                - List
                - Logout
                - Clear
                - Quit
                """;
    }

    @Override
    public void notify(ServerMessage notification) {
        System.out.println("it worked!");
    }
}
