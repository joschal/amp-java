package de.joschal.mdp.core.logic;

import de.joschal.mdp.core.entities.network.AbstractRouter;
import de.joschal.mdp.core.entities.protocol.Address;
import de.joschal.mdp.core.entities.protocol.Datagram;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class Router extends AbstractRouter {

    @Override
    public boolean forwardDatagram(Datagram datagram) {
        log.info("Received a datagram, which needs to be forwarded: {}", datagram);
        // TODO routing algorithm goes here
        return false;
    }

    @Override
    public boolean sendToNetwork(String message, Address destination) {

        Datagram datagram = new Datagram(node.getAddress(), destination, 42, 0, UUID.randomUUID().toString(), message);
        return node.getDataNetworkInterfaces().iterator().next().sendDatagram(datagram);
    }
}
