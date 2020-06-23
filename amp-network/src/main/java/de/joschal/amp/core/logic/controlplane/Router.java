package de.joschal.amp.core.logic.controlplane;

import de.joschal.amp.core.entities.messages.AbstractForwardableMessage;
import de.joschal.amp.core.entities.messages.routing.RouteDiscovery;
import de.joschal.amp.core.entities.network.addressing.Address;
import de.joschal.amp.core.entities.network.routing.AbstractRouter;
import de.joschal.amp.core.entities.network.routing.Route;
import de.joschal.amp.io.NetworkInterface;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
public class Router extends AbstractRouter {

    public Router(Address localAddress, List<NetworkInterface> networkInterfaces) {
        super(localAddress, new HashSet<>(), networkInterfaces);
    }

    /**
     * If there is no route known for a desired node, the route discovery procedure is invoked.
     * It floods all interfaces with route discovery messages, which recursively traverse the whole network
     *
     * @param destination Address of the node, which is to be discovered
     */
    public void sendRouteDiscovery(Address destination) {
        // sends a RouteDiscovery message via all available network interfaces
        networkInterfaces.forEach(networkInterface ->
                networkInterface.sendMessage(
                        new RouteDiscovery(localAddress, destination, DEFAULT_ROUTE_DISCOVERY_HOP_LIMIT)));
    }

    /**
     * Updates thr routing table by the values of areceived message.
     * Must be invoked for every received message to
     * This is the route/network optimisation logic
     *
     * @param networkInterface network interface from which the message was received
     * @param message          the received message with the necessary header values
     */
    public void updateRoutingTable(NetworkInterface networkInterface, AbstractForwardableMessage message) {

        // 0 is not a routable address and is therefore not stored
        if (message.getSourceAddress().getValue() == 0 ||
                message.getSourceAddress().getValue() == localAddress.getValue()) {
            return;
        }

        Optional<Route> optional = routingTable.stream()
                .filter(r -> r.getAddress().equals(message.getSourceAddress()))
                .findFirst();

        // a route to the source node is already known
        if (optional.isPresent()) {
            Route route = optional.get();

            // update the hop distance
            if (message.getHopCounter() < route.getHops()) {
                route.refresh(); // update creation date to now
                route.setHops(message.getHopCounter());

                // if a faster route is found via a different network interface, update it
                if (!networkInterface.equals(route.getNetworkInterface())) {
                    route.setNetworkInterface(networkInterface);
                }

            }

            // no route to source node known
            // create one
        } else {
            Route route = new Route(networkInterface, message.getSourceAddress(), message.getHopCounter());
            this.addRoute(route);
        }
    }

}
