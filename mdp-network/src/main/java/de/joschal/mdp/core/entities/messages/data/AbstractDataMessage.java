package de.joschal.mdp.core.entities.messages.data;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.messages.Forwardable;

public abstract class AbstractDataMessage extends AbstractMessage implements Forwardable {
    public AbstractDataMessage(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
