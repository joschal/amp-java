package de.joschal.mdp.core.logic.router;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.messages.data.Datagram;
import de.joschal.mdp.core.entities.network.AbstractRouter;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.entities.network.Route;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * This Router forwards Datagrams according to it's static routing table.
 * There is no
 */
@Slf4j
@Deprecated
public class NonFloodingRouter extends AbstractRouter {

    public NonFloodingRouter() {
        this.routingTable = new HashSet<>();
    }

    @Override
    protected List<AbstractMessage> forwardMessage(AbstractMessage message, NetworkInterface source) {
        log.info("[{}] Will try to forward message: {}", this.node.getAddress(), message);
        NetworkInterface networkInterface = getRoute(message.getDestinationAddress());

        if (networkInterface != null) {
            return networkInterface.sendMessage(message);
        }
        log.error("No route found for message {}", message);
        return Collections.emptyList();
    }

    @Override
    public List<AbstractMessage> sendDatagram(String message, Address destination) {
        return sendDatagram(message, destination, this.node.getInterfaces().get(0));
    }

    @Override
    public List<AbstractMessage> sendDatagram(String message, Address destination, NetworkInterface networkInterface) {
        return networkInterface.sendMessage(new Datagram(node.getAddress(), destination, 5, message));
    }

    private NetworkInterface getRoute(Address destination) {
        for (Route route : this.routingTable) {
            if (destination.equals(route.address)) {
                return route.networkInterface;
            }
        }
        return null;
    }

    @Override
    public List<AbstractMessage> sendMessage(AbstractMessage message) {
        log.info("[{}] Sending message: {}", this.node.getId(), message);
        NetworkInterface networkInterface = getRoute(message.getDestinationAddress());

        if (networkInterface != null) {
            return networkInterface.sendMessage(message);
        }
        log.error("No route found for message {}", message);
        return Collections.emptyList();
    }
}
