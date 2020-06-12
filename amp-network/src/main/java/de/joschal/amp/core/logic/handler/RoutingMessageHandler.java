package de.joschal.amp.core.logic.handler;

import de.joschal.amp.core.entities.AbstractForwardableMessage;
import de.joschal.amp.core.entities.AbstractMessage;
import de.joschal.amp.core.entities.Address;
import de.joschal.amp.core.entities.messages.routing.RouteDiscovery;
import de.joschal.amp.core.entities.messages.routing.RouteReply;
import de.joschal.amp.core.entities.network.NetworkInterface;
import de.joschal.amp.core.inbound.IForwardableMessageReceiver;
import de.joschal.amp.core.logic.sender.MessageSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class RoutingMessageHandler implements IForwardableMessageReceiver {

    private Address localAddress;
    private MessageSender messageSender;

    @Override
    public void handleMessage(AbstractForwardableMessage message, NetworkInterface source) {

        if (message instanceof RouteDiscovery) {
            respondToRouteDiscovery(message);
        } else if (message instanceof RouteReply) {
            respondToRouteReply(message);
        } else {
            log.error("not implemented");
        }


    }

    private Optional<AbstractMessage> respondToRouteReply(AbstractMessage message) {
        log.info("Received route reply message: {}", (message));
        return Optional.empty();
    }

    private Optional<AbstractMessage> respondToRouteDiscovery(AbstractMessage message) {

        log.info("received a route discovery message {}", message);
        messageSender.sendMessageViaKnownRoute(new RouteReply((RouteDiscovery) message));
        return Optional.empty();
    }
}
