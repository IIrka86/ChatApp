package kov.irok;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class Connection {

    private final Socket socket;
    private final BufferedWriter writer;
    private final BufferedReader reader;
    private Thread thread;
    private ConnectionListener listener;
    private String name;
    private boolean isAgent;
    private Connection connectionTo;

    final static Logger logger = Logger.getLogger(Connection.class);

    public Connection(ConnectionListener listener, String ipAddress, int port) throws IOException {
        this(listener, new Socket(ipAddress,port));
    }

    public Connection(ConnectionListener listener, Socket socket) throws IOException {
        this.listener = listener;
        this.socket = socket;
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    listener.onConnectionReady(Connection.this);
                    while (!thread.isInterrupted()){
                        listener.onReceiveString(Connection.this, reader.readLine());
                    }
                }catch (IOException e){
                    listener.onException(Connection.this, e);
                }finally {
                    listener.onDisconnect(Connection.this);
                }
            }
        });
        thread.start();
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized void setName(String name) {
        this.name = name;
    }

    public synchronized boolean isAgent() {
        return isAgent;
    }

    public synchronized void setAgent(boolean isAgent) {
        this.isAgent = isAgent;
    }

    public synchronized Connection getConnectionTo() {
        return connectionTo;
    }

    public synchronized void setConnectionTo(Connection connectionTo) {
        this.connectionTo = connectionTo;
    }

    public synchronized void sendString(String string){
        try{
            writer.write(string);
            writer.newLine();
            writer.flush();
        }catch (IOException e){
            listener.onException(Connection.this, e);
            disconnect();
        }
    }

    public synchronized String getString(){
        try{
            return reader.readLine();
        }catch (IOException e){
            listener.onException(Connection.this, e);
            disconnect();
        }
        return null;
    }

    public synchronized void disconnect(){
        thread.interrupt();
    }
}
