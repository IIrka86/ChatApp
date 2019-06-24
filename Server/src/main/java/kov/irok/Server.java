package kov.irok;

import kov.irok.users.Agent;
import kov.irok.users.Client;
import kov.irok.users.UserEvent;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server implements ConnectionListener{

    final static Logger logger = Logger.getLogger(Server.class); //Logger is not used int class. Logger is constant and should be all uppercase. Should be private.

    private final LinkedList<Connection> agents = new LinkedList<>();
    private final LinkedList<Connection> clients = new LinkedList<>();

    public static void main(String[] args) {
        new Server();
    }

    public Server() {
        try (ServerSocket server = new ServerSocket(8000)){

            logger.info("Server started at port 8000");

            while (true)
                try{
                    Socket socket = server.accept();
                    new Connection(this, socket);
                    logger.info("Connected client. IP: " + socket.getInetAddress());
                }catch (IOException e){
                    logger.error("Connection exception: " + e);
                }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(Connection connection) {
        String request;
        do {
            request = connection.getString();
        } while (!register(request,connection));
    }

    @Override
    public synchronized void onReceiveString(Connection connection, String string) {
        if(string.equals("/leave")){ // Leave command not implemented

        }else if(string.equals("/exit")){
            onDisconnect(connection);
            if(connection.isAgent()){
                    // Empty if - agent returning not implemented - More in README
            }
        }else{
            send(connection, string);
        }
    }

    @Override
    public synchronized void onDisconnect(Connection connection) {
        if (connection.isAgent()) {
            agents.remove(connection);
        }else{
            clients.remove(connection);
        }
        connection.disconnect();
        logger.info("Disconnect client " + connection.getName());
    }

    @Override
    public synchronized void onException(Connection connection, Exception e) {
        logger.error("Connection exception: " + e);
    }

    public synchronized boolean register(String request, Connection connection){ // Must be private - not used outside of the class
        //Welcome message with description of register message is missing.
        //Why length of the name is only 1 to 3? There are lot of foreign names with length of words more than 3.

        String[] strings = request.split(" ");
        if(strings.length < 3 || strings.length > 5 || !strings[0].equals("/register")){
            connection.sendString("Can not register user. Please try again!");
            logger.warn("Registration error");
            return false;
        }else if(strings[1].equals("agent")){
            registerUser(clients,agents,connection,strings, new Agent());
            return true;
        }else if (strings[1].equals("client")){
            registerUser(agents,clients,connection,strings, new Client());
            return true;
        }else{
            connection.sendString("Can not register user. Please try again!!");
            logger.warn("Registration error");
            return false;
        }
    }

    private void registerUser(LinkedList<Connection> connectTo, LinkedList<Connection> connectFrom,
                              Connection connection, String[] strings, UserEvent event){
        if (!connectTo.isEmpty()){
            event.register(connection, strings, connectTo.pop());
        }else{
            connectFrom.addLast(event.register(connection,strings,null));
        }
    }

    private void send(Connection connection, String string){ // Method name should be sendMsg
        if (connection.getConnectionTo()==null){
            connection.sendString("Wait your opponent please.");
        }else{
            connection.getConnectionTo().sendString(connection.getName()+ ": " +string);
        }
    }
}
