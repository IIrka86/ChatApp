package kov.irok;

import java.io.*;
import java.util.Scanner;

public class Client implements ConnectionListener{

    private Connection connection;

    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        try {
            connection = new Connection(this,"127.0.0.1",8000 );
            while (true){
                Scanner scanner = new Scanner(System.in);
                connection.sendString(scanner.nextLine());
            }
        } catch (IOException e) {
            System.out.println("Connection exception: " + e);
        }
    }

    @Override
    public void onConnectionReady(Connection connection) {
        System.out.println("Connection ready..");
    }

    @Override
    public void onReceiveString(Connection connection, String string) {
        System.out.println(string);
    }

    @Override
    public void onDisconnect(Connection connection) {
        System.out.println(",,"); // Disconnect message is missing.
    }

    @Override
    public void onException(Connection connection, Exception e) {
        System.out.println("Connection exception: " + e);
    }
}
