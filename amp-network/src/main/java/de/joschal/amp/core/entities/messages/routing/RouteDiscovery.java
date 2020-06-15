package de.joschal.amp.core.entities.messages.routing;

import de.joschal.amp.core.entities.Address;
import de.joschal.amp.core.logic.router.Router;

import java.util.LinkedList;

public class RouteDiscovery extends AbstractRoutingMessage {

    public RouteDiscovery(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, Router.DEFAULT_ROUTE_DISCOVERY_HOP_LIMIT);
    }

    @Override
    public String toString() {
        return "RouteDiscovery [" + getSourceAddress() + "] -> [" + getDestinationAddress() + "] with [" + getHopCounter() + "] hops via " + this.getTracerouteList().toString();
    }
}
