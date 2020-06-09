package de.joschal.amp.core.entities.messages.routing;

import de.joschal.amp.core.entities.messages.Forwardable;
import de.joschal.amp.core.entities.AbstractMessage;
import de.joschal.amp.core.entities.Address;

public abstract class AbstractRoutingMessage extends AbstractMessage implements Forwardable {
    public AbstractRoutingMessage(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
