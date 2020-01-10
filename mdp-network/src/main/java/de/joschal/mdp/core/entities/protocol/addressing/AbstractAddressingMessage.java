package de.joschal.mdp.core.entities.protocol.addressing;

import de.joschal.mdp.core.entities.protocol.AbstractMessage;
import de.joschal.mdp.core.entities.protocol.Address;

public abstract class AbstractAddressingMessage extends AbstractMessage {

    public AbstractAddressingMessage(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
