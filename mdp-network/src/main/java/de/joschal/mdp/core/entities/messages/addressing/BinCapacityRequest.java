package de.joschal.mdp.core.entities.messages.addressing;

import de.joschal.mdp.core.entities.Address;
import lombok.ToString;

@ToString
public class BinCapacityRequest extends AbstractAddressingMessage {
    public BinCapacityRequest(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}
