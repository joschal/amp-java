package de.joschal.mdp.core.logic.router;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.network.AbstractRouter;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.entities.network.Route;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Slf4j
public class FloodingRouter extends AbstractRouter {

    public FloodingRouter() {
        this.routingTable = new HashSet<>();
    }

    @Override
    protected List<AbstractMessage> forwardMessage(AbstractMessage datagram) {
        return null;
    }

    @Override
    public List<AbstractMessage> sendDatagram(String message, Address destination) {
        return null;
    }

    @Override
    public List<AbstractMessage> sendDatagram(String message, Address destination, NetworkInterface networkInterface) {
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

    // TODO refactor, using stream API
    private NetworkInterface getRoute(Address destination) {
        for (Route route : this.routingTable) {
            if (destination.equals(route.address)) {
                return route.networkInterface;
            }
        }
        return null;
    }
}
