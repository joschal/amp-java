package de.joschal.amp.core.logic.nodes;

import de.joschal.amp.core.entities.network.AbstractRouter;
import de.joschal.amp.core.entities.Address;
import de.joschal.amp.core.entities.AddressPool;
import de.joschal.amp.core.entities.network.AbstractNode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@ToString
public class SimpleNode extends AbstractNode {

    public SimpleNode(String id, AbstractRouter router, AddressPool... addressPools) {
        super(id, router, addressPools);
    }

    // For testing
    public void action(String message, Address destination) {
        log.info("Action triggered in node {}", this.id);
        this.router.sendDatagram(message, destination);
    }

}
