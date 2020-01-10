package de.joschal.mdp.core.entities.protocol.addressing;

import de.joschal.mdp.core.entities.protocol.Address;

public class PrefixAssigned extends AbstractAddressingMessage {
    public PrefixAssigned(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
