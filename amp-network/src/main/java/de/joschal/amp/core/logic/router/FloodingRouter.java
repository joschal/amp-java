package de.joschal.amp.core.logic.router;

import de.joschal.amp.core.entities.AbstractMessage;
import de.joschal.amp.core.entities.messages.Forwardable;
import de.joschal.amp.core.entities.messages.routing.RouteDiscovery;
import de.joschal.amp.core.entities.network.AbstractRouter;
import de.joschal.amp.core.entities.network.Route;
import de.joschal.amp.core.entities.Address;
import de.joschal.amp.core.entities.messages.data.Datagram;
import de.joschal.amp.core.entities.network.NetworkInterface;
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
    protected List<AbstractMessage> forwardMessage(Forwardable message, NetworkInterface source) {

        List<AbstractMessage> messages = new LinkedList<>();

        // Drop message if hop limit is reached
        if (((AbstractMessage) message).getHopCounter() >= ((AbstractMessage) message).getHopLimit()) {
            return messages;
        }

        // Drop message, if it came around in a loop
        if (((AbstractMessage) message).getSourceAddress().getValue() == this.node.getAddress().getValue()) {
            return messages;
        }

        getRoute((AbstractMessage) message).ifPresentOrElse(
                // Route is found
                route -> messages.addAll(
                        route.getNetworkInterface()
                                .sendMessage(message.cloneMessage())),

                // No route found -> Flooding
                () -> messages.addAll(
                        floodMessage(message.cloneMessage(), source))
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
    public Optional<Route> routeDiscovery(Address destination) {

        // sends a RouteDiscovery message via all available network interfaces
        this.node.getNetworkInterfaces().forEach(networkInterface ->
                networkInterface.sendMessage(
                        new RouteDiscovery(this.node.getAddress(), destination, DEFAULT_ROUTE_DISCOVERY_HOP_LIMIT)));

        // This checks, if a route to the desired destination could be found
        return getRoute(destination);
    }

    private List<AbstractMessage> floodMessage(AbstractMessage message, NetworkInterface source) {
        List<AbstractMessage> messages = new LinkedList<>();

        this.node.getNetworkInterfaces()
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
