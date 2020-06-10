package de.joschal.amp.core.entities.messages.routing;

import de.joschal.amp.core.entities.Address;
import de.joschal.amp.core.logic.router.Router;

import java.util.LinkedList;

public class RouteDiscovery extends AbstractRoutingMessage {

    public RouteDiscovery(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, Router.DEFAULT_ROUTE_DISCOVERY_HOP_LIMIT);
    }

    // Only used for cloning the object
    protected RouteDiscovery(RouteDiscovery original) {
        this(new Address(original.getSourceAddress().getValue()), new Address(original.getDestinationAddress().getValue()), original.getHopLimit());
        this.hopCounter = original.getHopCounter();
        this.tracerouteList = new LinkedList<>(original.tracerouteList);
    }

    @Override
    public String toString() {
        return "RouteReply [" + getSourceAddress() + "] -> [" + getDestinationAddress() + "] with [" + getHopCounter() + "] hops via " + this.getTracerouteList().toString();
    }
}
