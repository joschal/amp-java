package de.joschal.mdp.core.entities.protocol.control;

import de.joschal.mdp.core.entities.protocol.AbstractMessage;
import de.joschal.mdp.core.entities.protocol.Address;

public abstract class AbstractControlMessage extends AbstractMessage {
    public AbstractControlMessage(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
