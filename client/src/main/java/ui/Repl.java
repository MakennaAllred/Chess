package ui;

import model.AuthData;

import java.util.Scanner;

public class Repl {
    public static State state = State.SIGNEDOUT;
    public static AuthData auth;
    public static String username;
    public static int port;

    public Repl(int serverURL){
        this.port = serverURL;
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
                    res = PreLoginMenu.eval(port, line);
                } else if (state == State.SIGNEDIN) {
                    res = PostLoginMenu.eval(port, line);
                }else{
                    res = InGame.eval(port,line);
                }

            }
            catch(Throwable e){
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

}
