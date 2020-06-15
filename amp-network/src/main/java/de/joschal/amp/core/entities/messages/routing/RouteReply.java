package de.joschal.amp.core.entities.messages.routing;

import de.joschal.amp.core.entities.Address;

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

    @Override
    public String toString() {
        return "RouteReply [" + getSourceAddress() + "] -> [" + getDestinationAddress() + "] with [" + getHopCounter() + "] hops";
    }

}
