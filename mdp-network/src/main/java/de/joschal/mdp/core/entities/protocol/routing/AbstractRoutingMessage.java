package de.joschal.mdp.core.entities.protocol.routing;

import de.joschal.mdp.core.entities.protocol.AbstractMessage;
import de.joschal.mdp.core.entities.protocol.Address;

public abstract class AbstractRoutingMessage extends AbstractMessage {
    public AbstractRoutingMessage(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
