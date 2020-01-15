package de.joschal.mdp.core.entities.messages.addressing;

import de.joschal.mdp.core.entities.Address;
import lombok.ToString;

@ToString
public class BinCapacityReply extends AbstractAddressingMessage {
    public BinCapacityReply(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
