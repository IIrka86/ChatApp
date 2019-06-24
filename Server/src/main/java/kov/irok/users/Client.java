package kov.irok.users;

import kov.irok.Connection;
import org.apache.log4j.Logger;

public class Client implements UserEvent {

    final static private Logger logger = Logger.getLogger(Client.class); // Wrong order of keywords - see code convention. Rest of mistake same as in connection.

    @Override
    public Connection register(Connection client, String[] strings, Connection agent) {
        // Linking client and agent is happening not in time of 1st message sent.
        // Linking of client and agent should be after 1st message of client received.

        client.setName(getName(strings));
        client.setAgent(false);
        logger.info("Connected: client " + client.getName());
        if (agent!=null){
            establishConnection(client,agent);
            logger.info("Client " + client.getName() + " connected to agent " + agent.getName());
            return null;
        }else{
            client.sendString("All agents is busy. Please wait ...");
            client.sendString("If you want exit, please print /exit");
            return client;
        }
    }

    @Override
    public void leave(Connection connection) { // Method not implemented. Why had it been declared in interface?

    }

    @Override
    public void exit(Connection connection) { // Method not implemented. Why had it been declared in interface?

    }
}
