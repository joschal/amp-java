package de.joschal.mdp.core.logic;

import de.joschal.mdp.core.entities.network.AbstractNode;
import de.joschal.mdp.core.entities.network.AbstractRouter;
import de.joschal.mdp.core.entities.protocol.Address;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Node extends AbstractNode {

    public Node(Address address, AbstractRouter router) {
        super(address, router);
    }

    public boolean receiveFromNetwork(String message, Address source) {
        log.info("Received message from {} with content {}", source, message);
        return true;
    }

    // For testing
    public void action(String message, Address destnation) {
        this.router.sendToNetwork(message, destnation);
    }

}
