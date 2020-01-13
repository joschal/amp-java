package de.joschal.mdp.core.entities.messages.routing;

import de.joschal.mdp.core.entities.Address;

public class RouteReply extends AbstractRoutingMessage {
    public RouteReply(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
