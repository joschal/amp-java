package de.joschal.mdp.core.entities.protocol.routing;

import de.joschal.mdp.core.entities.protocol.Address;

public class RouteReply extends AbstractRoutingMessage {
    public RouteReply(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
