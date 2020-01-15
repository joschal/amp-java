package de.joschal.mdp.core.logic.router;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.messages.data.Datagram;
import de.joschal.mdp.core.entities.network.AbstractRouter;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.entities.network.Route;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class FloodingRouter extends AbstractRouter {

    public static final int HOP_LIMIT = 16;

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
        return sendMessage(new Datagram(this.node.getAddress(), destination, HOP_LIMIT, message));
    }

    @Override
    public List<AbstractMessage> sendDatagram(String message, Address destination, NetworkInterface networkInterface) {
        return null;
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

                // No route found -> Flooding
                () -> messages.addAll(
                        floodMessage(message, null))

        );

        return messages;
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

    // TODO refactor, using stream API
    private Optional<Route> getRoute(AbstractMessage message) {
        return routingTable.stream()
                .filter(r -> r.getAddress().equals(message.getDestinationAddress()))
                .findFirst();
    }
}
