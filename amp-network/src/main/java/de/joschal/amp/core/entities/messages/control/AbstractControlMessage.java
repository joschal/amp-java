package de.joschal.amp.core.entities.messages.control;

import de.joschal.amp.core.entities.messages.AbstractMessage;
import de.joschal.amp.core.entities.network.addressing.Address;

public abstract class AbstractControlMessage extends AbstractMessage {
    public AbstractControlMessage(Address sourceAddress, Address destinationAddress) {
        super(sourceAddress, destinationAddress);
    }
}
