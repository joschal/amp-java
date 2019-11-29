package de.joschal.mdp.core.logic.simple;

import de.joschal.mdp.core.entities.network.AbstractNode;
import de.joschal.mdp.core.entities.network.AbstractRouter;
import de.joschal.mdp.core.entities.protocol.Address;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleNode extends AbstractNode {


    public SimpleNode(String id, AbstractRouter router) {
        super(id, router);
    }

    public SimpleNode(String id, Address address, AbstractRouter router) {
        super(id, address, router);
    }

    public boolean receiveFromNetwork(String message, Address source) {
        log.info("[{}] Received message from {} with content {}", this.id, source, message);
        return true;
    }

    // For testing
    public void action(String message, Address destination) {
        log.info("Action triggered in node {}", this.id);
        this.router.sendToNetwork(message, destination);
    }

}
