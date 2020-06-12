package de.joschal.amp.core.logic.nodes;

import de.joschal.amp.core.entities.Address;
import de.joschal.amp.core.entities.AddressPool;
import de.joschal.amp.core.entities.messages.data.AbstractDataMessage;
import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.AbstractRouter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@ToString
public class SimpleNode extends AbstractNode {

    public SimpleNode(String id, AddressPool... addressPools) {
        super(id, addressPools);
    }

    @Override
    public void receiveDatagram(String payload, Address source) {
        log.info("Business logic received data message: {}", payload);

    }

    @Override
    public boolean receiveAcknowledgedDatagram(String payload, Address source) {
        log.info("Business logic received data message: {}", payload);
        return true;
    }

    // For testing
    public void action(String message, Address destination) {
        log.info("Action triggered in node {}", this.id);
        this.messageSender.sendMessageViaKnownRoute(null);
    }

}
