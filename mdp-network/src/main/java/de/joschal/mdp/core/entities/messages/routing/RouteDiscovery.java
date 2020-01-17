package de.joschal.mdp.core.entities.messages.routing;

import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.logic.router.FloodingRouter;
import lombok.ToString;

@ToString
public class RouteDiscovery extends AbstractRoutingMessage {

    public RouteDiscovery(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, FloodingRouter.DEFAULT_ROUTE_DISCOVERY_HOP_LIMIT);
    }
}
