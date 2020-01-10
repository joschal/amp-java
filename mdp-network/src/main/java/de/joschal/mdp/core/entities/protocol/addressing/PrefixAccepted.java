package de.joschal.mdp.core.entities.protocol.addressing;

import de.joschal.mdp.core.entities.protocol.Address;

public class PrefixAccepted extends AbstractAddressingMessage {
    public PrefixAccepted(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
