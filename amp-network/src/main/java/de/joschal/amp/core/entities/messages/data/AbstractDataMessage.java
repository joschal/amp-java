package de.joschal.amp.core.entities.messages.data;

import de.joschal.amp.core.entities.messages.Forwardable;
import de.joschal.amp.core.entities.AbstractMessage;
import de.joschal.amp.core.entities.Address;

public abstract class AbstractDataMessage extends AbstractMessage implements Forwardable {
    public AbstractDataMessage(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
