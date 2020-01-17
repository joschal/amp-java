package de.joschal.mdp.core.logic.router;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.messages.data.Datagram;
import de.joschal.mdp.core.entities.messages.routing.RouteDiscovery;
import de.joschal.mdp.core.entities.network.AbstractRouter;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.entities.network.Route;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class FloodingRouter extends AbstractRouter {

    public static final int DEFAULT_DATAGRAM_HOP_LIMIT = 16;
    public static final int DEFAULT_ROUTE_DISCOVERY_HOP_LIMIT = 16;

    public FloodingRouter() {
        this.routingTable = new HashSet<>();
    }

    @Override
    protected List<AbstractMessage> forwardMessage(AbstractMessage message, NetworkInterface source) {

        List<AbstractMessage> messages = new LinkedList<>();

        // Drop message if hop limit is reached
        if (message.getHopCounter() >= message.getHopLimit()) {
            return messages;
        }

        // Drop message, if it came around in a loop
        if (message.getSourceAddress().getValue() == this.node.getAddress().getValue()) {
            return messages;
        }

        getRoute(message).ifPresentOrElse(
                // Route is found
                route -> messages.addAll(
                        route.getNetworkInterface()
                                .sendMessage(message)),

                // No route found -> Flooding
                () -> messages.addAll(
                        floodMessage(message, source))
        );

        return messages;

    }

    @Override
    public List<AbstractMessage> sendDatagram(String message, Address destination) {
        return sendMessage(new Datagram(this.node.getAddress(), destination, DEFAULT_DATAGRAM_HOP_LIMIT, message));
    }

    @Override
    public List<AbstractMessage> sendDatagram(String message, Address destination, NetworkInterface networkInterface) {
        log.error("Not implemented");
        return Collections.emptyList();
    }

    /**
     * @param message Message to send
     * @return Either empty list or list of response messages
     */
    @Override
    public List<AbstractMessage> sendMessage(AbstractMessage message) {
        log.debug("[{}] Sending message: {}", this.node.getId(), message);

        List<AbstractMessage> messages = new LinkedList<>();
        getRoute(message).ifPresentOrElse(route ->
                        messages.addAll(
                                route.getNetworkInterface().sendMessage(message)),

                // No route found -> first, do route discovery
                () -> routeDiscovery(message.getDestinationAddress())
                        // If a route was found, send the message
                        .ifPresent(route ->
                                messages.addAll(
                                        route.getNetworkInterface().sendMessage(message))
                        )

        );

        return messages;
    }

    /**
     * If there is no route known for a desired node, the route discovery procedure is invoked.
     * It floods all interfaces with route discovery messages, which recursively traverse the whole network
     * TODO To avoid large numbers of messages clogging the network, this algorithm could be optimized by gradually incrementing the hop count
     *
     * @param destination Address of the node, which is to be discovered
     * @return Optional of a route. The contained route, is the best route, regarding to the hop count
     */
    private Optional<Route> routeDiscovery(Address destination) {

        RouteDiscovery routeDiscovery = new RouteDiscovery(this.node.getAddress(), destination, DEFAULT_ROUTE_DISCOVERY_HOP_LIMIT);

        NetworkInterface bestInterface = null;
        int bestHops = Integer.MAX_VALUE;

        for (NetworkInterface networkInterface : this.node.getInterfaces()) {
            List<AbstractMessage> messageList = networkInterface.sendMessage(routeDiscovery);

            for (AbstractMessage message : messageList) {
                if (message.getHopLimit() < bestHops) {
                    bestHops = message.getHopLimit();
                    bestInterface = networkInterface;
                }
            }

        }

        if (bestInterface != null) {
            Route route = new Route(bestInterface, destination, bestHops);
            this.addRoute(route);
            return Optional.of(route);
        }

        return Optional.empty();

    }

    private List<AbstractMessage> floodMessage(AbstractMessage message, NetworkInterface source) {
        List<AbstractMessage> messages = new LinkedList<>();

        this.node.getInterfaces()
                .stream()
                // Do not send the message to it's source
                .filter(networkInterface -> !networkInterface.equals(source))
                .forEach(networkInterface ->
                        messages.addAll(networkInterface.sendMessage(message)));

        return messages;
    }

    private Optional<Route> getRoute(AbstractMessage message) {
        return routingTable.stream()
                .filter(r -> r.getAddress().equals(message.getDestinationAddress()))
                .findFirst();
    }

    private Optional<Route> getRoute(Address destination) {
        return routingTable.stream()
                .filter(r -> r.getAddress().equals(destination))
                .findFirst();
    }
}
