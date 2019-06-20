package kov.irok.users;

import kov.irok.Connection;
import org.apache.log4j.Logger;

public class Agent implements UserEvent {

    final static private Logger logger = Logger.getLogger(Agent.class);

    @Override
    public Connection register(Connection agent, String[] strings, Connection client) {

        agent.setName(getName(strings));
        agent.setAgent(true);
        logger.info("Connected: agent " + agent.getName());
        if (client!=null){
            establishConnection(client,agent);
            logger.info("Client " + client.getName() + " connected to agent " + agent.getName());
            return null;
        }else{
            agent.sendString("Do not have clients. Please wait ...");
            return agent;
        }
    }

    @Override
    public void leave(Connection connection) {

    }

    @Override
    public void exit(Connection connection) {

    }
}
