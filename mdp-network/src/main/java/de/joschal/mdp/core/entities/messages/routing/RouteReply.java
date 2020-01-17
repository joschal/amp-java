package de.joschal.mdp.core.entities.messages.routing;

import lombok.ToString;

@ToString
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

}
