package de.joschal.mdp.core.entities.messages.control;

import de.joschal.mdp.core.entities.Address;

public class Goodbye extends AbstractControlMessage {
    public Goodbye(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
