package de.joschal.mdp.core.entities.messages.routing;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.messages.Forwardable;

public abstract class AbstractRoutingMessage extends AbstractMessage implements Forwardable {
    public AbstractRoutingMessage(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
