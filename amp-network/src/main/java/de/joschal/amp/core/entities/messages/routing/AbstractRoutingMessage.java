package de.joschal.amp.core.entities.messages.routing;

import de.joschal.amp.core.entities.AbstractForwardableMessage;
import de.joschal.amp.core.entities.Address;

public abstract class AbstractRoutingMessage extends AbstractForwardableMessage {
    public AbstractRoutingMessage(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
