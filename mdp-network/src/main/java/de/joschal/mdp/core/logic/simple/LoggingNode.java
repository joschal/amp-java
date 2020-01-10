package de.joschal.mdp.core.logic.simple;

import de.joschal.mdp.core.entities.network.AbstractNode;
import de.joschal.mdp.core.entities.network.AbstractRouter;
import de.joschal.mdp.core.entities.protocol.Address;
import de.joschal.mdp.core.entities.protocol.addressing.AbstractAddressingMessage;
import de.joschal.mdp.core.entities.protocol.control.AbstractControlMessage;
import de.joschal.mdp.core.entities.protocol.data.AbstractDataMessage;
import de.joschal.mdp.core.entities.protocol.routing.AbstractRoutingMessage;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
public class LoggingNode extends AbstractNode {


    public LoggingNode(String id, AbstractRouter router) {
        super(id, router);
    }

    public LoggingNode(String id, Address address, AbstractRouter router) {
        super(id, address, router);
    }

    // For testing
    public void action(String message, Address destination) {
        log.info("Action triggered in node {}", this.id);
        this.router.sendDatagram(message, destination);
    }

    @Override
    public boolean receiveAddressingMessage(AbstractAddressingMessage message) {
        log.info("[{}] Received message from {}: [{}]", this.id, message.getSourceAddress(), message);
        return true;
    }

    @Override
    public boolean receiveControlMessage(AbstractControlMessage message) {
        log.info("[{}] Received message from {}: [{}]", this.id, message.getSourceAddress(), message);
        return true;

    }

    @Override
    public boolean receiveDataMessage(AbstractDataMessage message) {
        log.info("[{}] Received message from {}: [{}]", this.id, message.getSourceAddress(), message);
        return true;

    }

    @Override
    public boolean receiveRoutingMessage(AbstractRoutingMessage message) {
        log.info("[{}] Received message from {}: [{}]", this.id, message.getSourceAddress(), message);
        return true;

    }
}
