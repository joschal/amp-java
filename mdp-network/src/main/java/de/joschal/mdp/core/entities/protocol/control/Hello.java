package de.joschal.mdp.core.entities.protocol.control;

import de.joschal.mdp.core.entities.protocol.Address;

public class Hello extends AbstractControlMessage {

    public Hello(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }

}
