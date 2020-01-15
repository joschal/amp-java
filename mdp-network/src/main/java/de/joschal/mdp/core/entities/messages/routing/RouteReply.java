package de.joschal.mdp.core.entities.messages.routing;

import de.joschal.mdp.core.entities.Address;
import lombok.ToString;

@ToString
public class RouteReply extends AbstractRoutingMessage {
    public RouteReply(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
