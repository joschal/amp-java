package de.joschal.mdp.core.entities.network;

import de.joschal.mdp.core.entities.protocol.Datagram;
import de.joschal.mdp.core.outbound.INetworkSender;

import java.util.Arrays;
import java.util.Set;

public abstract class AbstractRouter implements INetworkSender {

    protected AbstractNode node;

    private Set<Route> routingTable;

    void setNode(AbstractNode node) {
        this.node = node;
    }

    public void addRoute(Route... routes){
        routingTable.addAll(Arrays.asList(routes));
    }

    protected abstract boolean forwardDatagram(Datagram datagram);

}
