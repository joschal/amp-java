package de.joschal.mdp.core.logic.staticrouting;

import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.messages.data.AbstractDataMessage;
import de.joschal.mdp.core.entities.messages.data.Datagram;
import de.joschal.mdp.core.entities.network.AbstractRouter;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.entities.network.Route;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;

/**
 * This Router forwards Datagrams according to it's static routing table.
 * There is no
 */
@Slf4j
public class StaticRouter extends AbstractRouter {

    public StaticRouter() {
        this.routingTable = new HashSet<>();
    }

    @Override
    protected boolean forwardDatagram(AbstractDataMessage datagram) {
        log.info("[{}] Will try to forward datagram: {}", this.node.getAddress(), datagram);
        NetworkInterface networkInterface = getRoute(datagram.getDestinationAddress());

        if (networkInterface != null) {
            return networkInterface.sendMessage(datagram);
        }
        log.error("No route found for message {}", datagram);
        return false;
    }

    @Override
    public boolean sendDatagram(String message, Address destination) {
        return sendDatagram(message, destination, this.node.getInterfaces().get(0));
    }

    @Override
    public boolean sendDatagram(String message, Address destination, NetworkInterface networkInterface) {
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
}
