package de.joschal.amp.core.entities.messages.addressing;

import de.joschal.amp.core.entities.AbstractMessage;
import de.joschal.amp.core.entities.Address;

public abstract class AbstractAddressingMessage extends AbstractMessage {

    public AbstractAddressingMessage(Address sourceAddress, Address destinationAddress) {
        super(sourceAddress, destinationAddress);
    }
}
