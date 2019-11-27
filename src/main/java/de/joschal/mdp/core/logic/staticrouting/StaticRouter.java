package de.joschal.mdp.core.logic.staticrouting;

import de.joschal.mdp.core.entities.network.AbstractRouter;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.entities.network.Route;
import de.joschal.mdp.core.entities.protocol.Address;
import de.joschal.mdp.core.entities.protocol.Datagram;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;

@Slf4j
public class StaticRouter extends AbstractRouter {

    public StaticRouter() {
        this.routingTable = new HashSet<>();
    }

    @Override
    protected boolean forwardDatagram(Datagram datagram) {

        log.info("[{}] Will try to forward datagram: {}", this.node.getAddress(), datagram);
        NetworkInterface networkInterface = getRoute(datagram.getDestinationAddress());

        if (networkInterface != null) {
            return networkInterface.sendDatagram(datagram);
        }
        log.error("No route found for datagram {}", datagram);
        return false;
    }

    @Override
    public boolean sendToNetwork(String message, Address destination) {
        return sendToNetwork(message, destination, this.node.getInterfaces().get(0));
    }

    @Override
    public boolean sendToNetwork(String message, Address destination, NetworkInterface networkInterface) {
        return networkInterface.sendDatagram(new Datagram(node.getAddress(), destination, 5, message));
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
