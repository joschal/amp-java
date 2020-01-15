package de.joschal.mdp.core.logic.router;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.messages.data.Datagram;
import de.joschal.mdp.core.entities.network.AbstractRouter;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
public class NonForwardingRouter extends AbstractRouter {

    @Override
    public List<AbstractMessage> forwardMessage(AbstractMessage message) {
        log.info("[{}] Received a message, which needs to be forwarded: {}", this.node.getId(), message);
        // TODO routing algorithm goes here
        return Collections.emptyList();
    }

    @Override
    public List<AbstractMessage> sendDatagram(String message, Address destination) {
        AbstractMessage datagram = new Datagram(this.node.getAddress(), destination, 5, message);
        log.info("[{}] Will send datagram to Network: {}", this.node.getId(), datagram);
        return node.getInterfaces().iterator().next().sendMessage(datagram);
    }

    @Override
    public List<AbstractMessage> sendDatagram(String message, Address destination, NetworkInterface networkInterface) {
        return Collections.emptyList();
    }

    @Override
    public List<AbstractMessage> sendMessage(AbstractMessage abstractMessage) {
        return Collections.emptyList();
    }
}
