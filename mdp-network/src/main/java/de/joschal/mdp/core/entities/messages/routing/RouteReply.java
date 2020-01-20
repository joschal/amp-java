package de.joschal.mdp.core.entities.messages.routing;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.Address;

import java.util.LinkedList;

public class RouteReply extends AbstractRoutingMessage {

    /**
     * Creates RouteReply message, based on received RouteDiscovery message
     * Source/destination addresses are swapped
     * The hop counter of the discovery message is used as hop limit of the reply
     *
     * @param routeDiscovery Received RouteDiscovery message
     */
    public RouteReply(RouteDiscovery routeDiscovery) {
        super(routeDiscovery.getDestinationAddress(), routeDiscovery.getSourceAddress(), routeDiscovery.getHopCounter());
    }

    // Only used for cloning the object
    protected RouteReply(RouteReply original) {
        super(new Address(original.getSourceAddress().getValue()), new Address(original.getDestinationAddress().getValue()), original.getHopLimit());
        this.hopCounter = original.getHopCounter();
        this.tracerouteList = new LinkedList<>(original.tracerouteList);
    }

    @Override
    public String toString() {
        return "RouteReply [" + getSourceAddress() + "] -> [" + getDestinationAddress() + "] with [" + getHopCounter() + "] hops";
    }

    @Override
    public AbstractMessage cloneMessage() {
        return new RouteReply(this);
    }
}
