package de.joschal.mdp.core.logic.simple;

import de.joschal.mdp.core.entities.network.AbstractRouter;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.entities.protocol.Address;
import de.joschal.mdp.core.entities.protocol.Datagram;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NonForwardingRouter extends AbstractRouter {

    @Override
    public boolean forwardDatagram(Datagram datagram) {
        log.info("[{}] Received a datagram, which needs to be forwarded: {}", this.node.getId(), datagram);
        // TODO routing algorithm goes here
        return false;
    }

    @Override
    public boolean sendToNetwork(String message, Address destination) {
        Datagram datagram = new Datagram(this.node.getAddress(), destination, 5, message);
        log.info("[{}] Will send datagram to Network: {}", this.node.getId(), datagram);
        return node.getInterfaces().iterator().next().sendDatagram(datagram);
    }

    @Override
    public boolean sendToNetwork(String message, Address destination, NetworkInterface networkInterface) {
        return false;
    }
}
