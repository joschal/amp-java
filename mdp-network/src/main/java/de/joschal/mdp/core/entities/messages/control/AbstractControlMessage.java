package de.joschal.mdp.core.entities.messages.control;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.Address;

public abstract class AbstractControlMessage extends AbstractMessage {
    public AbstractControlMessage(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
