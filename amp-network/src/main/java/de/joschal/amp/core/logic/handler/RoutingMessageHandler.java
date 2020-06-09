package de.joschal.amp.core.logic.handler;

import de.joschal.amp.core.entities.AbstractMessage;
import de.joschal.amp.core.entities.messages.routing.RouteDiscovery;
import de.joschal.amp.core.entities.messages.routing.RouteReply;
import de.joschal.amp.core.inbound.INetworkReceiver;
import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.NetworkInterface;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class RoutingMessageHandler implements INetworkReceiver {

    private AbstractNode node;

    @Override
    public Optional<AbstractMessage> handleMessage(AbstractMessage message, NetworkInterface source) {

        if (message instanceof RouteDiscovery) {
            return handleRouteDiscovery(message);
        } else if (message instanceof RouteReply) {
            return handleRouteReply(message);
        }
        throw new RuntimeException("Something went wrong while resolving a control message");

    }

    private Optional<AbstractMessage> handleRouteReply(AbstractMessage message) {
        log.info("Received route reply message: {}", (message));
        return Optional.empty();
    }

    private Optional<AbstractMessage> handleRouteDiscovery(AbstractMessage message) {

        log.info("[{}] received a route discovery message {}", this.node.getId(), message);
        this.node.getRouter().sendMessage(new RouteReply((RouteDiscovery) message));
        return Optional.empty();
    }
}
