package de.joschal.mdp.core.entities.protocol.addressing;

import de.joschal.mdp.core.entities.protocol.Address;

public class PrefixAdvertisement extends AbstractAddressingMessage {
    public PrefixAdvertisement(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
