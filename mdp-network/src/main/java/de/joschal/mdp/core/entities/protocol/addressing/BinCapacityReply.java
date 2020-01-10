package de.joschal.mdp.core.entities.protocol.addressing;

import de.joschal.mdp.core.entities.protocol.Address;

public class BinCapacityReply extends AbstractAddressingMessage {
    public BinCapacityReply(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
