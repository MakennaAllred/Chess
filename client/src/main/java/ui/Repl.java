package ui;

import java.util.Scanner;

public class Repl {
    public static State state = State.SIGNEDOUT;

    public Repl(String serverURL){}
    public void run(){
        System.out.println("Welcome to Chess, sign in to start.");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while(!result.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();

            try{
                if(state == State.SIGNEDOUT){
                    result =  PreLoginMenu.eval(line);
                    System.out.print(result);
                } else if (state == State.SIGNEDIN) {
                    result =  PostLoginMenu.eval(line);
                    System.out.print(result);
                }else{
                    result =  InGame.eval("phase 6");
                    System.out.print(result);
                }

            }
            catch(Throwable e){
                System.out.print(e.getMessage());
            }
        }
        System.out.println();
    }

    private void printPrompt(){System.out.print("\n");}

    public String help(){
        if(state == State.SIGNEDOUT){
            return """
                    - Register <username, password, email>
                    - Login <username, password>
                    - Quit
                    """;
        }
        return """
                - Create Game <gameName>
                - Join Game <PlayerColor, gameID>
                - List Games
                - Logout
                - Clear All
                - Quit
                """;
    }

}
