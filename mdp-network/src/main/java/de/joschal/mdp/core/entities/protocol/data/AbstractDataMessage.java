package de.joschal.mdp.core.entities.protocol.data;

import de.joschal.mdp.core.entities.protocol.AbstractMessage;
import de.joschal.mdp.core.entities.protocol.Address;

public abstract class AbstractDataMessage extends AbstractMessage {
    public AbstractDataMessage(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
