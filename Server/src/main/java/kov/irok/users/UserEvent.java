package kov.irok.users;

import kov.irok.Connection;

public interface UserEvent {
    Connection register(Connection connectionFrom, String[] strings, Connection connectionTo);

    void leave(Connection connection); // Method is unimplemented and never used.
    void exit(Connection connection); // Method is unimplemented and never used.

    default String getName(String[] strings){
        StringBuffer name = new StringBuffer();
        for (int i = 2; i < strings.length; i++){
            name = name.append(strings[i] + " "); // Not required to assign value. Append is just enough.
        }
        return name.toString();
    }

    default void establishConnection (Connection connectionFrom, Connection connectionTo){
        connectionFrom.setConnectionTo(connectionTo);
        connectionTo.setConnectionTo(connectionFrom);
        connectionFrom.sendString("Agent " + connectionTo.getName() + " waiting your question");
        connectionTo.sendString("You connected to client " + connectionFrom.getName());
    }
}
