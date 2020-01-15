package de.joschal.mdp.core.entities.messages.routing;

import de.joschal.mdp.core.entities.Address;
import lombok.ToString;

@ToString
public class RouteDiscovery extends AbstractRoutingMessage {

    public RouteDiscovery(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
