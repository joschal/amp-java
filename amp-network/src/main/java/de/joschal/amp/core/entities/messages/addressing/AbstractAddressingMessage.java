package de.joschal.amp.core.entities.messages.addressing;

import de.joschal.amp.core.entities.messages.AbstractMessage;
import de.joschal.amp.core.entities.network.addressing.Address;

public abstract class AbstractAddressingMessage extends AbstractMessage {

    public AbstractAddressingMessage(Address sourceAddress, Address destinationAddress) {
        super(sourceAddress, destinationAddress);
    }
}
