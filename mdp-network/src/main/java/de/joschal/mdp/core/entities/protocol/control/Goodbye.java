package de.joschal.mdp.core.entities.protocol.control;

import de.joschal.mdp.core.entities.protocol.Address;

public class Goodbye extends AbstractControlMessage {
    public Goodbye(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
