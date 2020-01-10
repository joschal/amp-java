package de.joschal.mdp.core.entities.protocol.routing;

import de.joschal.mdp.core.entities.protocol.Address;

public class RouteDiscovery extends AbstractRoutingMessage {

    public RouteDiscovery(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
