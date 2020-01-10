package de.joschal.mdp.core.logic.simple;

import de.joschal.mdp.core.entities.network.AbstractRouter;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.entities.protocol.AbstractMessage;
import de.joschal.mdp.core.entities.protocol.Address;
import de.joschal.mdp.core.entities.protocol.data.AbstractDataMessage;
import de.joschal.mdp.core.entities.protocol.data.Datagram;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NonForwardingRouter extends AbstractRouter {

    @Override
    public boolean forwardDatagram(AbstractDataMessage datagram) {
        log.info("[{}] Received a datagram, which needs to be forwarded: {}", this.node.getId(), datagram);
        // TODO routing algorithm goes here
        return false;
    }

    @Override
    public boolean sendDatagram(String message, Address destination) {
        AbstractMessage datagram = new Datagram(this.node.getAddress(), destination, 5, message);
        log.info("[{}] Will send datagram to Network: {}", this.node.getId(), datagram);
        return node.getInterfaces().iterator().next().sendMessage(datagram);
    }

    @Override
    public boolean sendDatagram(String message, Address destination, NetworkInterface networkInterface) {
        return false;
    }
}
