package ui;

import java.util.Scanner;

public class ClientMain {


    public static void main(String[] args) {
        String serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        new Repl(8080).run();
    }
}



