package de.joschal.mdp.core.entities.messages.addressing;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.Address;

public abstract class AbstractAddressingMessage extends AbstractMessage {

    public AbstractAddressingMessage(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
