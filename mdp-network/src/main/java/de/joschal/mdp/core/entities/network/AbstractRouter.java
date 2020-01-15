package de.joschal.mdp.core.entities.network;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.messages.data.AbstractDataMessage;
import de.joschal.mdp.core.outbound.IDatagramSender;
import de.joschal.mdp.core.outbound.IMessageSender;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractRouter implements IDatagramSender, IMessageSender {

    protected AbstractNode node;

    protected Set<Route> routingTable;

    void setNode(AbstractNode node) {
        this.node = node;
    }

    public void addRoute(Route... routes) {
        routingTable.addAll(Arrays.asList(routes));
    }

    // Data Messages are the only message type to be forwarded
    protected abstract Optional<AbstractMessage> forwardDatagram(AbstractDataMessage datagram);

}
