package de.joschal.mdp.core.entities.network;

import de.joschal.mdp.core.entities.messages.data.AbstractDataMessage;
import de.joschal.mdp.core.outbound.IDatagramSender;

import java.util.Arrays;
import java.util.Set;

public abstract class AbstractRouter implements IDatagramSender {

    protected AbstractNode node;

    protected Set<Route> routingTable;

    void setNode(AbstractNode node) {
        this.node = node;
    }

    public void addRoute(Route... routes) {
        routingTable.addAll(Arrays.asList(routes));
    }

    // Data Messages are the only message type to be forwarded
    protected abstract boolean forwardDatagram(AbstractDataMessage datagram);

}
