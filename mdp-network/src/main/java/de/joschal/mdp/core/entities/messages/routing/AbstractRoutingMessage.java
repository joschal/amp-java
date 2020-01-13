package de.joschal.mdp.core.entities.messages.routing;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.Address;

public abstract class AbstractRoutingMessage extends AbstractMessage {
    public AbstractRoutingMessage(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
