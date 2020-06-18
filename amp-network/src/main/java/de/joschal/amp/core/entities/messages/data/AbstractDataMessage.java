package de.joschal.amp.core.entities.messages.data;

import de.joschal.amp.core.entities.messages.AbstractForwardableMessage;
import de.joschal.amp.core.entities.network.addressing.Address;

public abstract class AbstractDataMessage extends AbstractForwardableMessage {
    public AbstractDataMessage(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
